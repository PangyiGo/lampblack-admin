package com.osen.cloud.system.data_socket.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_lampblack.*;
import com.osen.cloud.common.enums.LampblackSensorCode;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.service.data.lampblack.*;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.data_socket.model.LampblackDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 10:22
 * Description: 设备上传数据处理服务层
 */
@Slf4j
@Service
public class LampblackService {

    // 实时数据格式
    private String[] realTimeSensorFlag = {"-Rtd", "-Flag"};

    // 分钟，小时，天数据格式
    private String[] othersSensorFlag = {"-Avg", "-Max", "-Min", "-Flag"};

    @Autowired
    private DataHistoryService dataHistoryService;

    @Autowired
    private DataMinuteService dataMinuteService;

    @Autowired
    private DataHourService dataHourService;

    @Autowired
    private DataDayService dataDayService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    /**
     * 处理实时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleRealTimeData(Map<String, Object> data, String connectionID) {
        // 实时数据实体
        DataHistory dataHistory = new DataHistory();
        // 设备号
        String device_no = (String) data.get("MN");
        dataHistory.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        dataHistory.setDateTime(localDateTime);
        // 数据封装
        for (LampblackSensorCode lampblackSensorCode : LampblackSensorCode.values()) {
            // 1表示通过名字上传数据，2表示通过编号上传数据
            // 参数名称
            boolean is_name_exist = CPData.containsKey(lampblackSensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleDataMapperToRealtime(lampblackSensorCode, dataHistory, CPData, 1);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(lampblackSensorCode.getCode() + realTimeSensorFlag[0]);
            if (is_code_exist) {
                handleDataMapperToRealtime(lampblackSensorCode, dataHistory, CPData, 2);
            }
        }
        // 风机，净化器状态
        dataHistory.setFanFlag(ConstUtil.OPEN_STATUS);
        dataHistory.setPurifierFlag(ConstUtil.OPEN_STATUS);

        // 是否存在数值超标，数据报警处理
        if (CPData.containsValue("F") || CPData.containsValue("D") || CPData.containsValue("T") || CPData.containsValue("B")) {
            AlarmHistory alarmHistory = new AlarmHistory();
            BeanUtil.copyProperties(dataHistory, alarmHistory);
            // 保存报警记录
            alarmHistoryService.insertAlarmData(alarmHistory);
            stringRedisTemplate.boundHashOps(ConstUtil.ALARM_KEY).put(alarmHistory.getDeviceNo(), JSON.toJSONString(alarmHistory));
            log.info("设备号：" + alarmHistory.getDeviceNo() + " 数据异常报警");
        } else {
            Boolean hasKey = stringRedisTemplate.boundHashOps(ConstUtil.ALARM_KEY).hasKey(dataHistory.getDeviceNo());
            if (hasKey)
                stringRedisTemplate.boundHashOps(ConstUtil.ALARM_KEY).delete(dataHistory.getDeviceNo());
        }

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.createNewTableName(ConstUtil.REALTIME_TB));
        // 插入数据
        dataHistoryService.insertRealtimeData(dataHistory);

        // 设备在线状态
        boolean hasKey = stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).hasKey(connectionID);
        if (!hasKey) {
            // 不存在ConnectionID，设置设备在线，并且设置数据在线状态为2
            stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).put(connectionID, dataHistory.getDeviceNo());
            deviceService.updateDeviceStatus(ConstUtil.OPEN_STATUS, dataHistory.getDeviceNo());
        }
        // 保存最新实时数据
        stringRedisTemplate.boundHashOps(ConstUtil.DATA_KEY).put(dataHistory.getDeviceNo(), JSON.toJSONString(dataHistory));
    }

    private void handleDataMapperToRealtime(LampblackSensorCode lampblackSensorCode, DataHistory dataHistory, Map<String, Object> CPData, int type) {
        BigDecimal data_value = null;
        String data_flag = null;
        if (type == 1) {
            data_value = new BigDecimal((String) CPData.get(lampblackSensorCode.getName() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(lampblackSensorCode.getName() + realTimeSensorFlag[1]);
        } else if (type == 2) {
            data_value = new BigDecimal((String) CPData.get(lampblackSensorCode.getCode() + realTimeSensorFlag[0]));
            data_flag = (String) CPData.get(lampblackSensorCode.getCode() + realTimeSensorFlag[1]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (lampblackSensorCode) {
            case PM:
                dataHistory.setPm(data_value);
                dataHistory.setPmFlag(data_flag);
                break;
            case NMHC:
                dataHistory.setNmhc(data_value);
                dataHistory.setNmhcFlag(data_flag);
                break;
            case LAMPBLACK:
                dataHistory.setLampblack(data_value);
                dataHistory.setLampblackFlag(data_flag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + lampblackSensorCode);
        }
    }

    /**
     * 处理分钟数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleMinuteData(Map<String, Object> data, String connectionID) {
        // 分钟数据实体
        DataMinute dataMinute = new DataMinute();
        // 设备号
        String device_no = (String) data.get("MN");
        dataMinute.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        dataMinute.setDateTime(localDateTime);
        // 数据封装
        LampblackDataModel lampblackDataModel = new LampblackDataModel();
        for (LampblackSensorCode lampblackSensorCode : LampblackSensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(lampblackSensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 1);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(lampblackSensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 2);
            }
        }
        BeanUtil.copyProperties(lampblackDataModel, dataMinute);
        // 风机，净化器状态
        dataMinute.setFanFlag(ConstUtil.OPEN_STATUS);
        dataMinute.setPurifierFlag(ConstUtil.OPEN_STATUS);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.createNewTableName(ConstUtil.MINUTE_TB));
        // 插入数据
        dataMinuteService.insertMinuteData(dataMinute);
    }

    /**
     * 处理小时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleHourData(Map<String, Object> data, String connectionID) {
        // 小时数据实体
        DataHour dataHour = new DataHour();
        // 设备号
        String device_no = (String) data.get("MN");
        dataHour.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        dataHour.setDateTime(localDateTime);
        // 数据封装
        LampblackDataModel lampblackDataModel = new LampblackDataModel();
        for (LampblackSensorCode lampblackSensorCode : LampblackSensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(lampblackSensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 1);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(lampblackSensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 2);
            }
        }
        BeanUtil.copyProperties(lampblackDataModel, dataHour);
        // 风机，净化器状态
        dataHour.setFanFlag(ConstUtil.OPEN_STATUS);
        dataHour.setPurifierFlag(ConstUtil.OPEN_STATUS);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.createNewTableName(ConstUtil.HOUR_TB));
        // 插入数据
        dataHourService.insertHourData(dataHour);
    }

    /**
     * 处理天数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleDayData(Map<String, Object> data, String connectionID) {
        // 天数据实体
        DataDay dataDay = new DataDay();
        // 设备号
        String device_no = (String) data.get("MN");
        dataDay.setDeviceNo(device_no);
        // 设备数据
        Map<String, Object> CPData = (Map<String, Object>) data.get("CP");
        // 上传时间
        LocalDateTime localDateTime = (LocalDateTime) CPData.get("DataTime");
        dataDay.setDateTime(localDateTime);
        // 数据封装
        LampblackDataModel lampblackDataModel = new LampblackDataModel();
        for (LampblackSensorCode lampblackSensorCode : LampblackSensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(lampblackSensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 1);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(lampblackSensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(lampblackSensorCode, lampblackDataModel, CPData, 2);
            }
        }
        BeanUtil.copyProperties(lampblackDataModel, dataDay);
        // 风机，净化器状态
        dataDay.setFanFlag(ConstUtil.OPEN_STATUS);
        dataDay.setPurifierFlag(ConstUtil.OPEN_STATUS);

        // 动态生成表名
        MybatisPlusConfig.TableName.set(ConstUtil.createNewTableName(ConstUtil.DAY_TB));
        //插入数据
        dataDayService.insertDayData(dataDay);
    }

    private void handleMapperToInterval(LampblackSensorCode lampblackSensorCode, LampblackDataModel lampblackDataModel, Map<String, Object> CPData, int type) {
        BigDecimal data_avg = null;
        BigDecimal data_max = null;
        BigDecimal data_min = null;
        String data_flag = null;
        if (type == 1) {
            data_avg = new BigDecimal((String) CPData.get(lampblackSensorCode.getName() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(lampblackSensorCode.getName() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(lampblackSensorCode.getName() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(lampblackSensorCode.getName() + othersSensorFlag[3]);
        } else if (type == 2) {
            data_avg = new BigDecimal((String) CPData.get(lampblackSensorCode.getCode() + othersSensorFlag[0]));
            data_max = new BigDecimal((String) CPData.get(lampblackSensorCode.getCode() + othersSensorFlag[1]));
            data_min = new BigDecimal((String) CPData.get(lampblackSensorCode.getCode() + othersSensorFlag[2]));
            data_flag = (String) CPData.get(lampblackSensorCode.getCode() + othersSensorFlag[3]);
        } else {
            log.info("插入类型错误异常");
        }
        switch (lampblackSensorCode) {
            case PM:
                lampblackDataModel.setPm(data_avg);
                lampblackDataModel.setPmMax(data_max);
                lampblackDataModel.setPmMin(data_min);
                lampblackDataModel.setPmFlag(data_flag);
                break;
            case NMHC:
                lampblackDataModel.setNmhc(data_avg);
                lampblackDataModel.setNmhcMax(data_max);
                lampblackDataModel.setNmhcMin(data_min);
                lampblackDataModel.setNmhcFlag(data_flag);
                break;
            case LAMPBLACK:
                lampblackDataModel.setLampblack(data_avg);
                lampblackDataModel.setLampblackMax(data_max);
                lampblackDataModel.setLampblackMin(data_min);
                lampblackDataModel.setLampblackFlag(data_flag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + lampblackSensorCode);
        }
    }
}

