package com.osen.cloud.system.dev_data.coldchain.controller;

import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.ColdChainHourService;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.coldchain.util.DataVO;
import com.osen.cloud.system.dev_data.coldchain.util.ExportExcelUtil;
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
 * Date: 2019-11-04
 * Time: 15:46
 * Description: 冷链设备分钟控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainHourController {

    @Autowired
    private ColdChainHourService coldChainHourService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    /**
     * 数据日期范围查询历史数据
     * 查询封装
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param deviceNo  设备号
     * @return 信息
     */
    private List<ColdChainHour> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<ColdChainHour> coldChainHours = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHour);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime( MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHour> chainHours = coldChainHourService.queryHistoryByDate(startDate, endDate, deviceNo);
            coldChainHours.addAll(chainHours);
        }
        return coldChainHours;
    }

    /**
     * 获取设备历史数据
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/coldchain/hour/query/{monitor}")
    public RestResult queryHistoryByDate(@RequestBody Map<String, Object> params, @PathVariable("monitor") String monitor) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<ColdChainHour> coldChainHours = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHour);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime( MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHour> chainHours = coldChainHourService.queryHistoryByDate(startDate, endDate, deviceNo, monitor);
            coldChainHours.addAll(chainHours);
        }
        List<DataVO> dataVOS = new ArrayList<>(0);
        for (ColdChainHour coldChainHour : coldChainHours) {
            DataVO dataVO = new DataVO();
            switch (monitor) {
                case "m01":
                    dataVO.setTemp(coldChainHour.getT01());
                    dataVO.setDamp(coldChainHour.getH01());
                    dataVO.setDateTime(coldChainHour.getDateTime());
                    break;
                case "m02":
                    dataVO.setTemp(coldChainHour.getT02());
                    dataVO.setDamp(coldChainHour.getH02());
                    dataVO.setDateTime(coldChainHour.getDateTime());
                    break;
                case "m03":
                    dataVO.setTemp(coldChainHour.getT03());
                    dataVO.setDamp(coldChainHour.getH03());
                    dataVO.setDateTime(coldChainHour.getDateTime());
                    break;
                case "m04":
                    dataVO.setTemp(coldChainHour.getT04());
                    dataVO.setDamp(coldChainHour.getH04());
                    dataVO.setDateTime(coldChainHour.getDateTime());
                    break;
            }
            dataVOS.add(dataVO);
        }
        return RestResultUtil.success(dataVOS);
    }

    /**
     * 数据导出到excel表格
     *
     * @param params   参数
     * @param response 响应
     */
    @PostMapping("/coldchain/hour/export")
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<ColdChainHour>> coldChains = new ArrayList<>(0); // 批量设备数据
        // 设备名称
        List<String> deviceNames = new ArrayList<>(0);
        // 冷链监控点
        List<ColdChainMonitor> coldChainMonitors = new ArrayList<>(0);
        for (String device : devices) {
            // 数据
            List<ColdChainHour> coldChainHours = this.wrapperDataQuery(startTime, endTime, device);
            coldChains.add(coldChainHours);
            // 设备名称
            Device dev = deviceService.findDeviceNo(device);
            deviceNames.add(dev.getName());
            // 监控点
            ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(device);
            coldChainMonitors.add(monitor);
        }

        // Excel表格导出
        try {
            HSSFWorkbook hssfWorkbook = ExportExcelUtil.hourExcelExport(coldChains, deviceNames, coldChainMonitors);

            String title = "冷链天数据表格";
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
            log.error("导出冷链天数据Excel表格异常");
            log.error(e.getMessage());
        }
    }
}
