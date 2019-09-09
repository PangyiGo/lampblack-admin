package com.osen.cloud.system.socket.server;

import com.osen.cloud.common.entity.DataHistory;
import com.osen.cloud.common.enums.SensorCode;
import com.osen.cloud.service.data.DataHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DataSegmentService {

    // 实时数据格式
    private String[] realTimeSensorFlag = {"-Rtd", "-Flag"};

    // 分钟，小时，天数据格式
    private String[] othersSensorFlag = {"-Avg", "-Max", "-Min", "-Flag"};

    @Autowired
    private DataHistoryService dataHistoryService;

    /**
     * 处理实时数据
     *
     * @param data 数据
     */
    public void handleRealTimeData(Map<String, Object> data) {
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
        dataHistory.setFanFlag(1);
        dataHistory.setPurifierFlag(1);

        log.info("insert one data to dataHistory");

        dataHistoryService.insertRealtimeData(dataHistory);
    }

    private void handleDataMapperToRealtime(SensorCode sensorCode, DataHistory dataHistory, Map<String, Object> CPData) {
        switch (sensorCode) {
            case PM:
                BigDecimal pm_value = new BigDecimal((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[0]));
                dataHistory.setPm(pm_value);
                dataHistory.setPmFlag((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[1]));
                break;
            case NMHC:
                BigDecimal nmhc_value = new BigDecimal((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[0]));
                dataHistory.setNmhc(nmhc_value);
                dataHistory.setNmhcFlag((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[1]));
                break;
            case LAMPBLACK:
                BigDecimal lampblack_value = new BigDecimal((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[0]));
                dataHistory.setLampblack(lampblack_value);
                dataHistory.setLampblackFlag((String) CPData.get(sensorCode.getName() + realTimeSensorFlag[1]));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sensorCode);
        }
    }

    /**
     * 处理分钟数据
     *
     * @param data 数据
     */
    public void handleMinuteData(Map<String, Object> data) {
        System.out.println(data);
    }

    /**
     * 处理小时数据
     *
     * @param data 数据
     */
    public void handleHourData(Map<String, Object> data) {
        System.out.println(data);
    }

    /**
     * 处理天数据
     *
     * @param data 数据
     */
    public void handleDayData(Map<String, Object> data) {
        System.out.println(data);
    }
}
