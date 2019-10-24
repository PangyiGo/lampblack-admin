package com.osen.cloud.system.data.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.osen.cloud.common.entity.dev_lampblack.DataDay;
import com.osen.cloud.common.entity.dev_lampblack.DataHistory;
import com.osen.cloud.common.entity.dev_lampblack.DataHour;
import com.osen.cloud.common.entity.dev_lampblack.DataMinute;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.service.data.lampblack.DataDayService;
import com.osen.cloud.service.data.lampblack.DataHistoryService;
import com.osen.cloud.service.data.lampblack.DataHourService;
import com.osen.cloud.service.data.lampblack.DataMinuteService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.data.util.ReturnDataIntegrityVO;
import com.osen.cloud.system.db_config.MybatisPlusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-14
 * Time: 15:35
 * Description: 数据统计分析控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataAnalysisController {

    @Autowired
    private DataHistoryService dataHistoryService;

    @Autowired
    private DataMinuteService dataMinuteService;

    @Autowired
    private DataHourService dataHourService;

    @Autowired
    private DataDayService dataDayService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 指定用户设备数据接收完整率统计
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/rate")
    public RestResult dataIntegrityRate(@RequestBody Map<String, Object> params) {
        List<ReturnDataIntegrityVO> returnDataIntegrityVOS = new ArrayList<>(0);
        // 参数接收
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startTime, dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(endTime, dateTimeFormatter);
        LocalDateTime start = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(), 23, 59, 59, 99999);
        // 生成数据表
        List<String> realTb = ConstUtil.queryTableName(start, end, ConstUtil.REALTIME_TB); //实时数据表
        List<String> minuteTb = ConstUtil.queryTableName(start, end, ConstUtil.MINUTE_TB); //分钟数据表
        List<String> hourTb = ConstUtil.queryTableName(start, end, ConstUtil.HOUR_TB); //小时数据表
        List<String> dayTb = ConstUtil.queryTableName(start, end, ConstUtil.DAY_TB); //天数据表
        // 计算时间
        Duration duration = Duration.between(start, end);
        long reals = duration.toMinutes();
        long minutes = reals / 10;
        long hours = duration.toHours();
        long days = duration.toDays() + 1;
        // 获取指定用户设备列表
        String account = SecurityUtil.getUsername();
        Map<String, Object> map = deviceService.finaAllDeviceToUser(account);
        List<Device> devices = (List<Device>) map.get("devices");
        if (devices.size() == 0)
            return RestResultUtil.success(returnDataIntegrityVOS);
        for (Device device : devices) {
            ReturnDataIntegrityVO returnDataIntegrityVO = new ReturnDataIntegrityVO();
            returnDataIntegrityVO.setDeviceNo(device.getDeviceNo());
            returnDataIntegrityVO.setDeviceName(device.getName());
            // 统计查询
            int realCount = 0;
            int minuteCount = 0;
            int hourCount = 0;
            int dayCount = 0;
            DecimalFormat df = new DecimalFormat("######0.00");
            for (String tableName : realTb) {
                if (ConstUtil.compareToTime(tableName))
                    continue;
                MybatisPlusConfig.TableName.set(tableName);
                LambdaQueryWrapper<DataHistory> wrapper = Wrappers.<DataHistory>lambdaQuery().eq(DataHistory::getDeviceNo, device.getDeviceNo()).between(DataHistory::getDateTime, start, end);
                int count = dataHistoryService.count(wrapper);
                realCount += count;
            }
            returnDataIntegrityVO.setRealCount(reals);
            returnDataIntegrityVO.setReal(realCount);
            returnDataIntegrityVO.setRealPercentage(df.format(((double) realCount / (double) reals) * 100) + " %");

            for (String tableName : minuteTb) {
                if (ConstUtil.compareToTime(tableName))
                    continue;
                MybatisPlusConfig.TableName.set(tableName);
                LambdaQueryWrapper<DataMinute> wrapper = Wrappers.<DataMinute>lambdaQuery().eq(DataMinute::getDeviceNo, device.getDeviceNo()).between(DataMinute::getDateTime, start, end);
                int count = dataMinuteService.count(wrapper);
                minuteCount += count;
            }
            returnDataIntegrityVO.setMinuteCount(minutes);
            returnDataIntegrityVO.setMinute(minuteCount);
            returnDataIntegrityVO.setMinutePercentage(df.format(((double) minuteCount / (double) minutes) * 100) + " %");

            for (String tableName : hourTb) {
                if (ConstUtil.compareToTime(tableName))
                    continue;
                MybatisPlusConfig.TableName.set(tableName);
                LambdaQueryWrapper<DataHour> wrapper = Wrappers.<DataHour>lambdaQuery().eq(DataHour::getDeviceNo, device.getDeviceNo()).between(DataHour::getDateTime, start, end);
                int count = dataHourService.count(wrapper);
                hourCount += count;
            }
            returnDataIntegrityVO.setHourCount(hours);
            returnDataIntegrityVO.setHour(hourCount);
            returnDataIntegrityVO.setHourPercentage(df.format(((double) hourCount / (double) hours) * 100) + " %");

            for (String tableName : dayTb) {
                if (ConstUtil.compareToTime(tableName))
                    continue;
                MybatisPlusConfig.TableName.set(tableName);
                LambdaQueryWrapper<DataDay> wrapper = Wrappers.<DataDay>lambdaQuery().eq(DataDay::getDeviceNo, device.getDeviceNo()).between(DataDay::getDateTime, start, end);
                int count = dataDayService.count(wrapper);
                dayCount += count;
            }
            returnDataIntegrityVO.setDayCount(days);
            returnDataIntegrityVO.setDay(dayCount);
            returnDataIntegrityVO.setDayPercentage(df.format(((double) dayCount / (double) days) * 100) + " %");

            // 保存
            returnDataIntegrityVOS.add(returnDataIntegrityVO);
        }
        return RestResultUtil.success(returnDataIntegrityVOS);
    }
}
