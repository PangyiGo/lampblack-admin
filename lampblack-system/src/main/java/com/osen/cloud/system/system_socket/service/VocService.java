package com.osen.cloud.system.system_socket.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_vocs.*;
import com.osen.cloud.common.enums.VocSensorCode;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.vocs.*;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.system_socket.model.VocDataModel;
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
 * Time: 11:32
 * Description: VOC设备服务处理类
 */
@Service
@Slf4j
public class VocService {

    // 实时数据格式
    private String[] realTimeSensorFlag = {"-Rtd", "-Flag"};

    // 分钟，小时，天数据格式
    private String[] othersSensorFlag = {"-Avg", "-Max", "-Min", "-Flag"};

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private VocHistoryService vocHistoryService;

    @Autowired
    private VocMinuteService vocMinuteService;

    @Autowired
    private VocHourService vocHourService;

    @Autowired
    private VocDayService vocDayService;

    @Autowired
    private VocAlarmService vocAlarmService;

    /**
     * 处理实时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleRealtimeData(Map<String, Object> data, String connectionID) {
        // voc实时数据实体
        VocHistory vocHistory = new VocHistory();
        // 设备号
        String device_no = (String) data.get("MN");
        vocHistory.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        vocHistory.setDateTime(localDateTime);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocHistory));
        // 防止数据重复添加
        if (vocHistoryService.getOneData(vocHistory.getDeviceNo(), vocHistory.getDateTime()) == null) {
            // 数据封装
            for (VocSensorCode vocSensorCode : VocSensorCode.values()) {
                // 1表示通过名字上传数据，2表示通过编号上传数据
                // 参数名称
                boolean is_name_exist = CPData.containsKey(vocSensorCode.getName() + realTimeSensorFlag[0]);
                if (is_name_exist) {
                    handleDataMapperToRealtime(vocSensorCode, vocHistory, CPData, 1);
                }
                // 参数编号
                boolean is_code_exist = CPData.containsKey(vocSensorCode.getCode() + realTimeSensorFlag[0]);
                if (is_code_exist) {
                    handleDataMapperToRealtime(vocSensorCode, vocHistory, CPData, 2);
                }
            }

            // 是否存在数值超标，数据报警处理
            if (CPData.containsValue("F") || CPData.containsValue("D") || CPData.containsValue("T") || CPData.containsValue("B")) {
                VocAlarm vocAlarm = new VocAlarm();
                // 数据复制
                BeanUtil.copyProperties(vocHistory, vocAlarm);
                // 保存报警记录
                vocAlarmService.insertAlarm(vocAlarm);
                stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).put(vocAlarm.getDeviceNo(), JSON.toJSONString(vocAlarm));
                log.info("设备号：" + vocAlarm.getDeviceNo() + " 数据异常报警");
            } else {
                // 删除报警缓存记录
                Boolean hasKey = stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).hasKey(vocHistory.getDeviceNo());
                if (hasKey)
                    stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).delete(vocHistory.getDeviceNo());
            }

            // 插入数据
            vocHistoryService.insertHistory(vocHistory);
        }

        // 设备在线状态
        stringRedisTemplate.boundHashOps(TableUtil.Voc_Conn).put(connectionID, vocHistory.getDeviceNo());
        deviceService.updateDeviceStatus(ConstUtil.OPEN_STATUS, vocHistory.getDeviceNo());
        // 保存最新实时数据
        stringRedisTemplate.boundHashOps(TableUtil.Voc_RealTime).put(vocHistory.getDeviceNo(), JSON.toJSONString(vocHistory));
    }

    private void handleDataMapperToRealtime(VocSensorCode vocSensorCode, VocHistory vocHistory, Map<String, Object> CPData, int type) {
        BigDecimal data_value = null;
        String data_flag = null;
        if (type == 1) {
            data_value = new BigDecimal((String) CPData.get(vocSensorCode.getName() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(vocSensorCode.getName() + realTimeSensorFlag[1]);
        } else if (type == 2) {
            data_value = new BigDecimal((String) CPData.get(vocSensorCode.getCode() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(vocSensorCode.getCode() + realTimeSensorFlag[1]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (vocSensorCode) {
            case VOC:
                vocHistory.setVoc(data_value);
                vocHistory.setVocFlag(data_flag);
                break;
            case FLOW:
                vocHistory.setFlow(data_value);
                vocHistory.setFlowFlag(data_flag);
                break;
            case SPEED:
                vocHistory.setSpeed(data_value);
                vocHistory.setSpeedFlag(data_flag);
                break;
            case PRESSURE:
                vocHistory.setPressure(data_value);
                vocHistory.setPressureFlag(data_flag);
                break;
            case TEMP:
                vocHistory.setTemp(data_value);
                vocHistory.setTempFlag(data_flag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + vocSensorCode);
        }
    }

    /**
     * 处理分钟数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleMinuteData(Map<String, Object> data, String connectionID) {
        // VOC分钟数据实体
        VocMinute vocMinute = new VocMinute();
        // 设备号
        String device_no = (String) data.get("MN");
        vocMinute.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        vocMinute.setDateTime(localDateTime);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocMinute));
        // 防止数据重复添加
        if (vocMinuteService.getOneData(vocMinute.getDeviceNo(), vocMinute.getDateTime()) == null) {
            // 数据封装
            VocDataModel vocDataModel = new VocDataModel();
            // 数据封装
            for (VocSensorCode vocSensorCode : VocSensorCode.values()) {
                // 1表示通过名字上传数据，2表示通过编号上传数据
                // 参数名称
                boolean is_name_exist = CPData.containsKey(vocSensorCode.getName() + realTimeSensorFlag[0]);
                if (is_name_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 1);
                }
                // 参数编号
                boolean is_code_exist = CPData.containsKey(vocSensorCode.getCode() + realTimeSensorFlag[0]);
                if (is_code_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 2);
                }
            }

            BeanUtil.copyProperties(vocDataModel, vocMinute);
            // 插入数据
            vocMinuteService.insertMinute(vocMinute);
        }
    }

    /**
     * 处理小时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleHourData(Map<String, Object> data, String connectionID) {
        // VOC小时数据实体
        VocHour vocHour = new VocHour();
        // 设备号
        String device_no = (String) data.get("MN");
        vocHour.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        vocHour.setDateTime(localDateTime);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocHour));
        // 防止数据重复添加
        if (vocHourService.getOneData(vocHour.getDeviceNo(), vocHour.getDateTime()) == null) {
            // 数据封装
            VocDataModel vocDataModel = new VocDataModel();
            // 数据封装
            for (VocSensorCode vocSensorCode : VocSensorCode.values()) {
                // 1表示通过名字上传数据，2表示通过编号上传数据
                // 参数名称
                boolean is_name_exist = CPData.containsKey(vocSensorCode.getName() + realTimeSensorFlag[0]);
                if (is_name_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 1);
                }
                // 参数编号
                boolean is_code_exist = CPData.containsKey(vocSensorCode.getCode() + realTimeSensorFlag[0]);
                if (is_code_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 2);
                }
            }

            BeanUtil.copyProperties(vocDataModel, vocHour);
            // 插入数据
            vocHourService.insertHour(vocHour);
        }
    }

    /**
     * 处理天数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleDayData(Map<String, Object> data, String connectionID) {
        // VOC天数据实体
        VocDay vocDay = new VocDay();
        // 设备号
        String device_no = (String) data.get("MN");
        vocDay.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        vocDay.setDateTime(localDateTime);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocDay));
        // 防止数据重复插入
        if (vocDayService.getOneData(vocDay.getDeviceNo(), vocDay.getDateTime()) == null) {
            // 数据封装
            VocDataModel vocDataModel = new VocDataModel();
            // 数据封装
            for (VocSensorCode vocSensorCode : VocSensorCode.values()) {
                // 1表示通过名字上传数据，2表示通过编号上传数据
                // 参数名称
                boolean is_name_exist = CPData.containsKey(vocSensorCode.getName() + realTimeSensorFlag[0]);
                if (is_name_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 1);
                }
                // 参数编号
                boolean is_code_exist = CPData.containsKey(vocSensorCode.getCode() + realTimeSensorFlag[0]);
                if (is_code_exist) {
                    handleMapperToInterval(vocSensorCode, vocDataModel, CPData, 2);
                }
            }

            BeanUtil.copyProperties(vocDataModel, vocDay);
            // 插入数据
            vocDayService.insertDay(vocDay);
        }
    }

    private void handleMapperToInterval(VocSensorCode vocSensorCode, VocDataModel vocDataModel, Map<String, Object> CPData, int type) {
        BigDecimal data_avg = null;
        BigDecimal data_max = null;
        BigDecimal data_min = null;
        String data_flag = null;
        if (type == 1) {
            data_avg = new BigDecimal((String) CPData.get(vocSensorCode.getName() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(vocSensorCode.getName() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(vocSensorCode.getName() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(vocSensorCode.getName() + othersSensorFlag[3]);
        } else if (type == 2) {
            data_avg = new BigDecimal((String) CPData.get(vocSensorCode.getCode() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(vocSensorCode.getCode() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(vocSensorCode.getCode() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(vocSensorCode.getCode() + othersSensorFlag[3]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (vocSensorCode) {
            case VOC:
                vocDataModel.setVoc(data_avg);
                vocDataModel.setVocMax(data_max);
                vocDataModel.setVocMin(data_min);
                vocDataModel.setVocFlag(data_flag);
                break;
            case FLOW:
                vocDataModel.setFlow(data_avg);
                vocDataModel.setFlowMax(data_max);
                vocDataModel.setFlowMin(data_min);
                vocDataModel.setFlowFlag(data_flag);
                break;
            case SPEED:
                vocDataModel.setSpeed(data_avg);
                vocDataModel.setSpeedMax(data_max);
                vocDataModel.setSpeedMin(data_min);
                vocDataModel.setSpeedFlag(data_flag);
                break;
            case PRESSURE:
                vocDataModel.setPressure(data_avg);
                vocDataModel.setPressureMax(data_max);
                vocDataModel.setPressureMin(data_min);
                vocDataModel.setPressureFlag(data_flag);
                break;
            case TEMP:
                vocDataModel.setTemp(data_avg);
                vocDataModel.setTempMax(data_max);
                vocDataModel.setTempMin(data_min);
                vocDataModel.setTempFlag(data_flag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + vocSensorCode);
        }
    }

}
