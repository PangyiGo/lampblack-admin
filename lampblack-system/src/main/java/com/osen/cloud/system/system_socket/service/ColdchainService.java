package com.osen.cloud.system.system_socket.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_coldchain.*;
import com.osen.cloud.common.enums.ColdChainSensorCode;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.*;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.system_socket.model.ColdChainDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-06
 * Time: 14:04
 * Description: 冷链设备数据服务类
 */
@Service
@Slf4j
public class ColdchainService {

    // 实时数据格式
    private String[] realTimeSensorFlag = {"-Rtd", "-Flag"};

    // 分钟，小时，天数据格式
    private String[] othersSensorFlag = {"-Avg", "-Max", "-Min", "-Flag"};

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ColdChainHistoryService coldChainHistoryService;

    @Autowired
    private ColdChainMinuteService coldChainMinuteService;

    @Autowired
    private ColdChainHourService coldChainHourService;

    @Autowired
    private ColdChainDayService coldChainDayService;

    @Autowired
    private ColdChainAlarmService coldChainAlarmService;

    /**
     * 处理实时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleRealtimeData(Map<String, Object> data, String connectionID) {
        // 冷链实时数据实体
        ColdChainHistory coldChainHistory = new ColdChainHistory();
        // 设备号
        String device_no = (String) data.get("MN");
        coldChainHistory.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        coldChainHistory.setDateTime(localDateTime);
        // 数据封装
        for (ColdChainSensorCode coldChainSensorCode : ColdChainSensorCode.values()) {
            // 1表示通过名字上传数据，2表示通过编号上传数据
            // 参数名称
            boolean is_name_exist = CPData.containsKey(coldChainSensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleDataMapperToRealtime(coldChainSensorCode, coldChainHistory, CPData, 1);
            }
        }

        // 是否存在数值超标，数据报警处理
        if (CPData.containsValue("F") || CPData.containsValue("D") || CPData.containsValue("T") || CPData.containsValue("B")) {
            ColdChainAlarm coldChainAlarm = new ColdChainAlarm();
            // 数据复制
            BeanUtil.copyProperties(coldChainHistory, coldChainAlarm);
            // 保存报警记录
            coldChainAlarmService.insertAlarm(coldChainAlarm);
            stringRedisTemplate.boundHashOps(TableUtil.Cold_Alarm).put(coldChainAlarm.getDeviceNo(), JSON.toJSONString(coldChainAlarm));
            log.info("设备号：" + coldChainAlarm.getDeviceNo() + " 数据异常报警");
        } else {
            // 删除报警缓存记录
            Boolean hasKey = stringRedisTemplate.boundHashOps(TableUtil.Cold_Alarm).hasKey(coldChainHistory.getDeviceNo());
            if (hasKey)
                stringRedisTemplate.boundHashOps(TableUtil.Cold_Alarm).delete(coldChainHistory.getDeviceNo());
        }

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdHistory));
        // 插入数据
        coldChainHistoryService.insertHistory(coldChainHistory);

        // 设备在线状态
        stringRedisTemplate.boundHashOps(TableUtil.Cold_Conn).put(connectionID, coldChainHistory.getDeviceNo());
        deviceService.updateDeviceStatus(ConstUtil.OPEN_STATUS, coldChainHistory.getDeviceNo());
        // 保存最新实时数据
        stringRedisTemplate.boundHashOps(TableUtil.Cold_RealTime).put(coldChainHistory.getDeviceNo(), JSON.toJSONString(coldChainHistory));
    }

    private void handleDataMapperToRealtime(ColdChainSensorCode coldChainSensorCode, ColdChainHistory coldChainHistory, Map<String, Object> CPData, int type) {
        BigDecimal data_value = null;
        String data_flag = null;
        if (type == 1) {
            data_value = new BigDecimal((String) CPData.get(coldChainSensorCode.getName() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(coldChainSensorCode.getName() + realTimeSensorFlag[1]);
        } else if (type == 2) {
            data_value = new BigDecimal((String) CPData.get(coldChainSensorCode.getCode() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(coldChainSensorCode.getCode() + realTimeSensorFlag[1]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (coldChainSensorCode) {
            case T01:
                coldChainHistory.setT01(data_value);
                coldChainHistory.setT01Flag(data_flag);
                break;
            case H01:
                coldChainHistory.setH01(data_value);
                coldChainHistory.setH01Flag(data_flag);
                break;
            case T02:
                coldChainHistory.setT02(data_value);
                coldChainHistory.setT02Flag(data_flag);
                break;
            case H02:
                coldChainHistory.setH02(data_value);
                coldChainHistory.setH02Flag(data_flag);
                break;
            case T03:
                coldChainHistory.setT03(data_value);
                coldChainHistory.setT03Flag(data_flag);
                break;
            case H03:
                coldChainHistory.setH03(data_value);
                coldChainHistory.setH03Flag(data_flag);
                break;
            case T04:
                coldChainHistory.setT04(data_value);
                coldChainHistory.setT04Flag(data_flag);
                break;
            case H04:
                coldChainHistory.setH04(data_value);
                coldChainHistory.setH04Flag(data_flag);
                break;
            case Longitude:
                coldChainHistory.setLongitude(String.valueOf(data_value));
                break;
            case Latitude:
                coldChainHistory.setLatitude(String.valueOf(data_value));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + coldChainSensorCode);
        }
    }

    /**
     * 处理分钟数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleMinuteData(Map<String, Object> data, String connectionID) {
        // 冷链分钟数据实体
        ColdChainMinute coldChainMinute = new ColdChainMinute();
        // 设备号
        String device_no = (String) data.get("MN");
        coldChainMinute.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        coldChainMinute.setDateTime(localDateTime);
        // 数据封装
        ColdChainDataModel coldChainDataModel = new ColdChainDataModel();
        // 数据封装
        for (ColdChainSensorCode coldChainSensorCode : ColdChainSensorCode.values()) {
            // 1表示通过名字上传数据，2表示通过编号上传数据
            // 参数名称
            boolean is_name_exist = CPData.containsKey(coldChainSensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(coldChainSensorCode, coldChainDataModel, CPData, 1);
            }
        }

        BeanUtil.copyProperties(coldChainDataModel, coldChainMinute);
        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdMinute));
        // 插入数据
        coldChainMinuteService.insertMinute(coldChainMinute);
    }

    /**
     * 处理小时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleHourData(Map<String, Object> data, String connectionID) {
        // 冷链小时数据实体
        ColdChainHour coldChainHour = new ColdChainHour();
        // 设备号
        String device_no = (String) data.get("MN");
        coldChainHour.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        coldChainHour.setDateTime(localDateTime);
        // 数据封装
        ColdChainDataModel coldChainDataModel = new ColdChainDataModel();
        // 数据封装
        for (ColdChainSensorCode coldChainSensorCode : ColdChainSensorCode.values()) {
            // 1表示通过名字上传数据，2表示通过编号上传数据
            // 参数名称
            boolean is_name_exist = CPData.containsKey(coldChainSensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(coldChainSensorCode, coldChainDataModel, CPData, 1);
            }
        }

        BeanUtil.copyProperties(coldChainDataModel, coldChainHour);
        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdHour));
        // 插入数据
        coldChainHourService.insertHour(coldChainHour);
    }

    /**
     * 处理天数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleDayData(Map<String, Object> data, String connectionID) {
        // 冷链天数据实体
        ColdChainDay coldChainDay = new ColdChainDay();
        // 设备号
        String device_no = (String) data.get("MN");
        coldChainDay.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        coldChainDay.setDateTime(localDateTime);
        // 数据封装
        ColdChainDataModel coldChainDataModel = new ColdChainDataModel();
        // 数据封装
        for (ColdChainSensorCode coldChainSensorCode : ColdChainSensorCode.values()) {
            // 1表示通过名字上传数据，2表示通过编号上传数据
            // 参数名称
            boolean is_name_exist = CPData.containsKey(coldChainSensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(coldChainSensorCode, coldChainDataModel, CPData, 1);
            }
        }

        BeanUtil.copyProperties(coldChainDataModel, coldChainDay);
        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdDay));
        // 插入数据
        coldChainDayService.insertDay(coldChainDay);
    }

    private void handleMapperToInterval(ColdChainSensorCode coldChainSensorCode, ColdChainDataModel coldChainDataModel, Map<String, Object> CPData, int type) {
        BigDecimal data_avg = null;
        BigDecimal data_max = null;
        BigDecimal data_min = null;
        String data_flag = null;
        if (type == 1) {
            data_avg = new BigDecimal((String) CPData.get(coldChainSensorCode.getName() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(coldChainSensorCode.getName() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(coldChainSensorCode.getName() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(coldChainSensorCode.getName() + othersSensorFlag[3]);
        } else if (type == 2) {
            data_avg = new BigDecimal((String) CPData.get(coldChainSensorCode.getCode() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(coldChainSensorCode.getCode() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(coldChainSensorCode.getCode() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(coldChainSensorCode.getCode() + othersSensorFlag[3]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (coldChainSensorCode) {
            case T01:
                coldChainDataModel.setT01(data_avg);
                coldChainDataModel.setT01Max(data_max);
                coldChainDataModel.setT01Min(data_min);
                coldChainDataModel.setT01Flag(data_flag);
                break;
            case H01:
                coldChainDataModel.setH01(data_avg);
                coldChainDataModel.setH01Max(data_max);
                coldChainDataModel.setH01Min(data_min);
                coldChainDataModel.setH01Flag(data_flag);
                break;
            case T02:
                coldChainDataModel.setT02(data_avg);
                coldChainDataModel.setT02Max(data_max);
                coldChainDataModel.setT02Min(data_min);
                coldChainDataModel.setT02Flag(data_flag);
                break;
            case H02:
                coldChainDataModel.setH02(data_avg);
                coldChainDataModel.setH02Max(data_max);
                coldChainDataModel.setH02Min(data_min);
                coldChainDataModel.setH02Flag(data_flag);
                break;
            case T03:
                coldChainDataModel.setT03(data_avg);
                coldChainDataModel.setT03Max(data_max);
                coldChainDataModel.setT03Min(data_min);
                coldChainDataModel.setT03Flag(data_flag);
                break;
            case H03:
                coldChainDataModel.setH03(data_avg);
                coldChainDataModel.setH03Max(data_max);
                coldChainDataModel.setH03Min(data_min);
                coldChainDataModel.setH03Flag(data_flag);
                break;
            case T04:
                coldChainDataModel.setT04(data_avg);
                coldChainDataModel.setT04Max(data_max);
                coldChainDataModel.setT04Min(data_min);
                coldChainDataModel.setT04Flag(data_flag);
                break;
            case H04:
                coldChainDataModel.setH04(data_avg);
                coldChainDataModel.setH04Max(data_max);
                coldChainDataModel.setH04Min(data_min);
                coldChainDataModel.setH04Flag(data_flag);
                break;
            case Longitude:
                coldChainDataModel.setLongitude(String.valueOf(data_avg));
                break;
            case Latitude:
                coldChainDataModel.setLatitude(String.valueOf(data_avg));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + coldChainSensorCode);
        }
    }
}
