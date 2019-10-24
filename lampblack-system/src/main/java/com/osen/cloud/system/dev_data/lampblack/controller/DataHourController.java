package com.osen.cloud.system.dev_data.lampblack.controller;

import com.osen.cloud.common.entity.dev_lampblack.DataHour;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.lampblack.DataHourService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.dev_data.lampblack.util.ExportExcelUtil;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * Date: 2019-09-27
 * Time: 16:18
 * Description: 设备小时数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataHourController {

    @Autowired
    private DataHourService dataHourService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 查询小时数据历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/hour/queryByDate")
    public RestResult queryDataHistoryByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<DataHour> dataHours = this.wrapperDataQuery(startTime, endTime, deviceNo);
        return RestResultUtil.success(dataHours);
    }

    /**
     * 导出小时数据到Excel表格
     *
     * @param params   参数
     * @param response 参数
     */
    @PostMapping("/data/hour/exportToExcel")
    public void exportDataHourToExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<DataHour>> dataHours = new ArrayList<>(0);
        // 查询设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            List<DataHour> lists = this.wrapperDataQuery(startTime, endTime, device);
            dataHours.add(lists);
            // 设备名称
            Device dd = deviceService.findDeviceNo(device);
            deviceNames.add(dd.getName());
        }

        try {
            HSSFWorkbook excelToDataHistory = ExportExcelUtil.createExcelToDataHour(dataHours, deviceNames);

            String title = "小时数据表格";
            String fileName = new String(title.getBytes("GB2312"), "ISO_8859_1");
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("exportExcelCode", String.valueOf(ConstUtil.OK));

            OutputStream os = response.getOutputStream();
            excelToDataHistory.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.info("导出小时数据Excel表格异常");
            log.info(e.getMessage());
        }
    }

    /**
     * 查询指定设备的范围日期查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param deviceNo  设备号
     * @return 信息
     */
    private List<DataHour> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<DataHour> dataHours = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, ConstUtil.HOUR_TB);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<DataHour> history = dataHourService.queryDataHourByDate(startDate, endDate, deviceNo);
            dataHours.addAll(history);
        }
        return dataHours;
    }
}
