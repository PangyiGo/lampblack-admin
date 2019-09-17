package com.osen.cloud.system.socket.server;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.*;
import com.osen.cloud.common.enums.SensorCode;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.service.data.*;
import com.osen.cloud.service.device.DeviceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
public class DataSegmentService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AlarmHistoryService alarmHistoryService;

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

    /**
     * 处理实时数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleRealTimeData(Map<String, Object> data, String connectionID) {
        System.out.println(data);
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
        for (SensorCode sensorCode : SensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(sensorCode.getName() + realTimeSensorFlag[0]);
            if (is_name_exist) {
                handleDataMapperToRealtime(sensorCode, dataHistory, CPData);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(sensorCode.getCode() + realTimeSensorFlag[0]);
            if (is_code_exist) {
                handleDataMapperToRealtime(sensorCode, dataHistory, CPData);
            }
        }
        // 风机，净化器状态
        dataHistory.setFanFlag(ConstUtil.OPEN_STATUS);
        dataHistory.setPurifierFlag(ConstUtil.OPEN_STATUS);

        log.info("存储设备上传实时数据到数据库");
        log.info(dataHistory.toString());

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

        // 插入数据
        dataHistoryService.insertRealtimeData(dataHistory);

        // 设备在线状态
        boolean hasKey = stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).hasKey(connectionID);
        if (!hasKey) {
            // 不存在ConnectionID，设置设备在线
            stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).put(connectionID, dataHistory.getDeviceNo());
            deviceService.updateDeviceStatus(ConstUtil.OPEN_STATUS, dataHistory.getDeviceNo());
        }
        stringRedisTemplate.boundHashOps(ConstUtil.DATA_KEY).put(dataHistory.getDeviceNo(), JSON.toJSONString(dataHistory));
    }

    private void handleDataMapperToRealtime(SensorCode sensorCode, DataHistory dataHistory, Map<String, Object> CPData) {
        BigDecimal data_value = new BigDecimal((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[0]));
        String data_flag = (String) CPData.get(sensorCode.getName() + realTimeSensorFlag[1]);
        switch (sensorCode) {
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
                throw new IllegalStateException("Unexpected value: " + sensorCode);
        }
    }

    /**
     * 处理分钟数据
     *
     * @param data         数据
     * @param connectionID 连接ID
     */
    public void handleMinuteData(Map<String, Object> data, String connectionID) {
        System.out.println(data);
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
        DataModel dataModel = new DataModel();
        for (SensorCode sensorCode : SensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(sensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(sensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
        }
        BeanUtil.copyProperties(dataModel, dataMinute);
        // 风机，净化器状态
        dataMinute.setFanFlag(ConstUtil.OPEN_STATUS);
        dataMinute.setPurifierFlag(ConstUtil.OPEN_STATUS);

        log.info("存储设备上传分钟数据到数据库");
        log.info(dataMinute.toString());

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
        System.out.println(data);
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
        DataModel dataModel = new DataModel();
        for (SensorCode sensorCode : SensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(sensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(sensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
        }
        BeanUtil.copyProperties(dataModel, dataHour);
        // 风机，净化器状态
        dataHour.setFanFlag(ConstUtil.OPEN_STATUS);
        dataHour.setPurifierFlag(ConstUtil.OPEN_STATUS);

        log.info("存储设备上传小时数据到数据库");
        log.info(dataHour.toString());

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
        System.out.println(data);
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
        DataModel dataModel = new DataModel();
        for (SensorCode sensorCode : SensorCode.values()) {
            // 参数名称
            boolean is_name_exist = CPData.containsKey(sensorCode.getName() + othersSensorFlag[0]);
            if (is_name_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
            // 参数编号
            boolean is_code_exist = CPData.containsKey(sensorCode.getCode() + othersSensorFlag[0]);
            if (is_code_exist) {
                handleMapperToInterval(sensorCode, dataModel, CPData);
            }
        }
        BeanUtil.copyProperties(dataModel, dataDay);
        // 风机，净化器状态
        dataDay.setFanFlag(ConstUtil.OPEN_STATUS);
        dataDay.setPurifierFlag(ConstUtil.OPEN_STATUS);

        log.info("存储设备每天数据到数据库");
        log.info(dataDay.toString());

        //插入数据
        dataDayService.insertDayData(dataDay);
    }

    private void handleMapperToInterval(SensorCode sensorCode, DataModel dataModel, Map<String, Object> CPData) {
        BigDecimal data_avg = new BigDecimal((String) CPData.get(sensorCode.getName() + othersSensorFlag[0]));
        BigDecimal data_max = new BigDecimal((String) CPData.get(sensorCode.getName() + othersSensorFlag[1]));
        BigDecimal data_min = new BigDecimal((String) CPData.get(sensorCode.getName() + othersSensorFlag[2]));
        String data_flag = (String) CPData.get(sensorCode.getName() + othersSensorFlag[3]);
        switch (sensorCode) {
            case PM:
                dataModel.setPm(data_avg);
                dataModel.setPmMax(data_max);
                dataModel.setPmMin(data_min);
                dataModel.setPmFlag(data_flag);
                break;
            case NMHC:
                dataModel.setNmhc(data_avg);
                dataModel.setNmhcMax(data_max);
                dataModel.setNmhcMin(data_min);
                dataModel.setNmhcFlag(data_flag);
                break;
            case LAMPBLACK:
                dataModel.setLampblack(data_avg);
                dataModel.setLampblackMax(data_max);
                dataModel.setLampblackMin(data_min);
                dataModel.setLampblackFlag(data_flag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sensorCode);
        }
    }
}

/**
 * 转换中间件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class DataModel implements Serializable {

    private BigDecimal lampblack;

    private BigDecimal lampblackMin;

    private BigDecimal lampblackMax;

    private String lampblackFlag;

    private BigDecimal pm;

    private BigDecimal pmMin;

    private BigDecimal pmMax;

    private String pmFlag;

    private BigDecimal nmhc;

    private BigDecimal nmhcMin;

    private BigDecimal nmhcMax;

    private String nmhcFlag;

    private Integer fanFlag;

    private Integer purifierFlag;
}
