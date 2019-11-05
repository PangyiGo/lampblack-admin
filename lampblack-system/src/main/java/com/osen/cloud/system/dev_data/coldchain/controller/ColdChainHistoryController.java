package com.osen.cloud.system.dev_data.coldchain.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.ColdChainHistoryService;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.coldchain.util.AddressVO;
import com.osen.cloud.system.dev_data.coldchain.util.ExportExcelUtil;
import com.osen.cloud.system.dev_data.coldchain.util.RealTimeVO;
import com.osen.cloud.system.dev_data.coldchain.util.TodayVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 11:07
 * Description: 冷链设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainHistoryController {

    @Autowired
    private ColdChainHistoryService coldChainHistoryService;

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/coldchain/realtime/{deviceNo}")
    public RestResult getRealtime(@PathVariable("deviceNo") String deviceNo) {
        ColdChainHistory realtime = coldChainHistoryService.getRealtime(deviceNo);
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        RealTimeVO realTimeVO = new RealTimeVO();
        if (realtime != null)
            BeanUtil.copyProperties(realtime, realTimeVO);
        if (monitor != null) {
            BeanUtil.copyProperties(monitor, realTimeVO);
        } else {
            realTimeVO.setM01("未定义#1");
            realTimeVO.setM02("未定义#2");
            realTimeVO.setM03("未定义#3");
            realTimeVO.setM03("未定义#4");
        }
        return RestResultUtil.success(realTimeVO);
    }

    /**
     * 获取指定设备号的当天实施历史数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/coldchain/today/{deviceNo}")
    public RestResult getDataToday(@PathVariable("deviceNo") String deviceNo) {
        // 设置表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdHistory));
        List<ColdChainHistory> chainHistories = coldChainHistoryService.getDataToday(deviceNo);
        List<TodayVO> todayVOS = new ArrayList<>(0);
        for (ColdChainHistory chainHistory : chainHistories) {
            TodayVO todayVO = new TodayVO();
            BeanUtil.copyProperties(chainHistory, todayVO);
            todayVOS.add(todayVO);
        }
        List<Object> data = new ArrayList<>(0);
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        if (monitor == null) {
            monitor.setM01("未定义#1");
            monitor.setM02("未定义#2");
            monitor.setM03("未定义#3");
            monitor.setM03("未定义#4");
        }
        data.add(monitor.getM01());
        data.add(monitor.getM02());
        data.add(monitor.getM03());
        data.add(monitor.getM04());
        // 历史数据
        data.add(todayVOS);
        return RestResultUtil.success(data);
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
    private List<ColdChainHistory> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<ColdChainHistory> coldChainHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHistory> chainHistories = coldChainHistoryService.queryHistoryByDate(startDate, endDate, deviceNo);
            coldChainHistories.addAll(chainHistories);
        }
        return coldChainHistories;
    }

    /**
     * 获取设备历史数据
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/coldchain/realtime/query/{monitor}")
    public RestResult queryHistoryByDate(@RequestBody Map<String, Object> params, @PathVariable("monitor") String monitor) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<ColdChainHistory> coldChainHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHistory> chainHistories = coldChainHistoryService.queryHistoryByDate(startDate, endDate, deviceNo, monitor);
            coldChainHistories.addAll(chainHistories);
        }
        return RestResultUtil.success(coldChainHistories);
    }

    /**
     * 查询冷链设备历史轨迹
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/coldchain/locus")
    public RestResult getLocus(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<ColdChainHistory> coldChainHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHistory> locusToUser = coldChainHistoryService.getLocusToUser(startDate, endDate, deviceNo);
            coldChainHistories.addAll(locusToUser);
        }
        // 返回结果
        List<AddressVO> addressVOS = new ArrayList<>(0);
        for (ColdChainHistory coldChainHistory : coldChainHistories) {
            AddressVO addressVO = new AddressVO();
            BeanUtil.copyProperties(coldChainHistory, addressVO);
            addressVOS.add(addressVO);
        }
        return RestResultUtil.success(addressVOS);
    }

    /**
     * 数据导出到excel表格
     *
     * @param params   参数
     * @param response 响应
     */
    @PostMapping("/coldchain/realtime/export")
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<ColdChainHistory>> coldChains = new ArrayList<>(0); // 批量设备数据
        // 设备名称
        List<String> deviceNames = new ArrayList<>(0);
        // 冷链监控点
        List<ColdChainMonitor> coldChainMonitors = new ArrayList<>(0);
        for (String device : devices) {
            // 数据
            List<ColdChainHistory> chainHistories = this.wrapperDataQuery(startTime, endTime, device);
            coldChains.add(chainHistories);
            // 设备名称
            Device dev = deviceService.findDeviceNo(device);
            deviceNames.add(dev.getName());
            // 监控点
            ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(device);
            coldChainMonitors.add(monitor);
        }

        // Excel表格导出
        try {
            HSSFWorkbook hssfWorkbook = ExportExcelUtil.historyExcelExport(coldChains, deviceNames, coldChainMonitors);

            String title = "实时数据表格";
            String fileName = new String(title.getBytes("GB2312"), "ISO_8859_1");
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("exportExcelCode", String.valueOf(ConstUtil.OK));

            OutputStream os = response.getOutputStream();
            hssfWorkbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出实时数据Excel表格异常");
            log.error(e.getMessage());
        }
    }

    /**
     * 查新当前用户的设备实时数据
     *
     * @return 信息
     */
    @PostMapping("/coldchain/realtime/batch")
    public RestResult getBatchRealtime() {
        List<RealTimeVO> realTimeVOS = new ArrayList<>(0);
        List<ColdChainHistory> realtimeToUser = coldChainHistoryService.getRealtimeToUser();
        for (ColdChainHistory coldChainHistory : realtimeToUser) {
            RealTimeVO realTimeVO = new RealTimeVO();
            ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(coldChainHistory.getDeviceNo());
            if (monitor == null) {
                realTimeVO.setM01("未定义#1");
                realTimeVO.setM02("未定义#2");
                realTimeVO.setM03("未定义#3");
                realTimeVO.setM03("未定义#4");
            }
            BeanUtil.copyProperties(coldChainHistory, realTimeVO);
            BeanUtil.copyProperties(monitor, realTimeVO);
            realTimeVOS.add(realTimeVO);
        }
        return RestResultUtil.success(realTimeVOS);
    }

}
