package com.osen.cloud.system.wechat;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_vocs.VocAlarm;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.DeviceType;
import com.osen.cloud.common.enums.VocSensorCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.vocs.VocAlarmService;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.vocs.util.TodayVO;
import com.osen.cloud.system.wechat.model.VocMapModel;
import com.osen.cloud.system.wechat.requestVo.HistoryDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2020-01-15
 * Time: 9:34
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class VocWxController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VocHistoryService vocHistoryService;

    @Autowired
    private VocAlarmService vocAlarmService;

    /**
     * 获取当前用户的VOC全部设备列表
     *
     * @return 信息
     */
    @PostMapping("/wechat/voc/devices")
    public RestResult getVocDeviceToUsername() {
        List<Device> deviceList = deviceService.findAllToMap(DeviceType.Voc.getName());
        return RestResultUtil.success(deviceList);
    }

    /**
     * 根据设备号获取设备实时值
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/wechat/voc/rt/{deviceNo}")
    public RestResult getVocDeviceToRealtime(@PathVariable("deviceNo") String deviceNo) {
        VocHistory realtime = vocHistoryService.getRealtime(deviceNo);
        if (realtime == null) {
            return RestResultUtil.success(new VocHistory());
        }
        return RestResultUtil.success(realtime);
    }

    /**
     * 获取voc近12小时历史数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/wechat/voc/lately/{deviceNo}")
    public RestResult getVocHistory(@PathVariable("deviceNo") String deviceNo) {
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocHistory));
        List<VocHistory> vocHistoryList = vocHistoryService.getVocHistory("voc", deviceNo);
        List<TodayVO> todayVOS = new ArrayList<>(0);
        // 默认查询VOC
        String args = "voc";
        for (VocHistory vocHistory : vocHistoryList) {
            TodayVO todayVO = new TodayVO();
            switch (args) {
                case "voc":
                    todayVO.setData(vocHistory.getVoc());
                    break;
                case "flow":
                    todayVO.setData(vocHistory.getFlow());
                    break;
                case "speed":
                    todayVO.setData(vocHistory.getSpeed());
                    break;
                case "pressure":
                    todayVO.setData(vocHistory.getPressure());
                    break;
                case "temp":
                    todayVO.setData(vocHistory.getTemp());
                    break;
            }
            todayVO.setDateTime(vocHistory.getDateTime());
            todayVOS.add(todayVO);
        }
        return RestResultUtil.success(todayVOS);
    }

    /**
     * 获取当前用户的实时数据标记地图
     *
     * @return 信息
     */
    @PostMapping("/wechat/voc/maps")
    public RestResult getRealtimeToMap() {
        List<VocMapModel> vocMapModelList = new ArrayList<>(0);
        // 设备列表
        List<Device> deviceList = deviceService.findAllToMap(DeviceType.Voc.getName());
        for (Device device : deviceList) {
            VocMapModel mapModel = new VocMapModel();
            BeanUtil.copyProperties(device, mapModel);
            // 实时数据
            VocHistory realtime = vocHistoryService.getRealtime(device.getDeviceNo());
            if (realtime != null) {
                BeanUtil.copyProperties(realtime, mapModel);
            }
            vocMapModelList.add(mapModel);
        }
        return RestResultUtil.success(vocMapModelList);
    }

    /**
     * 获取设备的参数
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/wechat/voc/sensor/{deviceNo}")
    public RestResult getVocSensor(@PathVariable("deviceNo") String deviceNo) {
        List<String> sensor = new ArrayList<>(0);
        VocHistory realtime = vocHistoryService.getRealtime(deviceNo);
        if (realtime == null) {
            for (VocSensorCode code : VocSensorCode.values()) {
                sensor.add(code.getDesc());
            }
        } else {
            if (realtime.getVoc() != null && realtime.getVoc().compareTo(new BigDecimal(0)) >= 0) {
                sensor.add(VocSensorCode.VOC.getDesc());
            }
            if (realtime.getFlow() != null && realtime.getFlow().compareTo(new BigDecimal(0)) >= 0) {
                sensor.add(VocSensorCode.FLOW.getDesc());
            }
            if (realtime.getSpeed() != null && realtime.getSpeed().compareTo(new BigDecimal(0)) >= 0) {
                sensor.add(VocSensorCode.SPEED.getDesc());
            }
            if (realtime.getTemp() != null && realtime.getTemp().compareTo(new BigDecimal(0)) >= 0) {
                sensor.add(VocSensorCode.TEMP.getDesc());
            }
            if (realtime.getPressure() != null && realtime.getPressure().compareTo(new BigDecimal(0)) >= 0) {
                sensor.add(VocSensorCode.PRESSURE.getDesc());
            }
        }
        return RestResultUtil.success(sensor);
    }

    /**
     * 数据日期范围查询历史数据
     * 查询封装
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param deviceNo  设备号
     * @return 信息
     */
    private List<VocHistory> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<VocHistory> vocHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.DATE);
        // 开始时间
        LocalDate startDates = LocalDate.parse(startTime, dateTimeFormatter);
        LocalDateTime startDate = LocalDateTime.of(startDates.getYear(), startDates.getMonthValue(), startDates.getDayOfMonth(), 0, 0, 0);
        // 结束时间
        LocalDate endDates = LocalDate.parse(endTime, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.of(endDates.getYear(), endDates.getMonthValue(), endDates.getDayOfMonth(), 23, 59, 59);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.VocHistory);
        for (String tableName : queryTableName) {
            MybatisPlusConfig.TableName.set(tableName);
            List<VocHistory> vocHistoryList = vocHistoryService.queryHistoryByDate(startDate, endDate, deviceNo);
            vocHistories.addAll(vocHistoryList);
        }
        return vocHistories;
    }

    /**
     * 获取设备的历史数据
     *
     * @param historyDataVo 请求体
     * @return 信息
     */
    @PostMapping("/wechat/voc/history")
    public RestResult getVocHistoryData(@RequestBody HistoryDataVo historyDataVo) {
        List<TodayVO> todayVOList = new ArrayList<>(0);
        // 数据获取
        List<VocHistory> vocHistories = this.wrapperDataQuery(historyDataVo.getStartTime(), historyDataVo.getEndTime(), historyDataVo.getDeviceNo());
        for (VocHistory vocHistory : vocHistories) {
            TodayVO todayVO = new TodayVO();
            switch (historyDataVo.getSensor()) {
                case "voc":
                    todayVO.setData(vocHistory.getVoc());
                    break;
                case "flow":
                    todayVO.setData(vocHistory.getFlow());
                    break;
                case "speed":
                    todayVO.setData(vocHistory.getSpeed());
                    break;
                case "pressure":
                    todayVO.setData(vocHistory.getPressure());
                    break;
                case "temp":
                    todayVO.setData(vocHistory.getTemp());
                    break;
            }
            todayVO.setDateTime(vocHistory.getDateTime());
            todayVOList.add(todayVO);
        }
        return RestResultUtil.success(todayVOList);
    }

    /**
     * 获取当前用户的设备报警信息
     *
     * @return 信息
     */
    @PostMapping("/wechat/voc/alarm")
    public RestResult getVocDeviceAlarm() {
        // 报警数据
        String username = SecurityUtil.getUsername();
        List<VocAlarm> realtimeAlarm = vocAlarmService.getRealtimeAlarm(username);
        return RestResultUtil.success(realtimeAlarm);
    }
}
