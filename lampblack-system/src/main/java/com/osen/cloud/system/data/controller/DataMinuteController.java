package com.osen.cloud.system.data.controller;

import com.osen.cloud.common.entity.DataMinute;
import com.osen.cloud.common.entity.Device;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.DataMinuteService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.data.util.ExportExcelUtil;
import com.osen.cloud.system.db_config.MybatisPlusConfig;
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
 * Time: 16:14
 * Description: 设备分钟数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataMinuteController {

    @Autowired
    private DataMinuteService dataMinuteService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 查询分钟数据历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/minute/queryByDate")
    public RestResult queryDataMinuteByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<DataMinute> dataMinutes = this.wrapperDataQuery(startTime, endTime, deviceNo);
        return RestResultUtil.success(dataMinutes);
    }

    /**
     * 导出分钟数据到Excel表格
     *
     * @param params   参数
     * @param response 参数
     */
    @PostMapping("/data/minute/exportToExcel")
    public void exportDataMinuteToExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<DataMinute>> dataMinutes = new ArrayList<>(0);
        // 查询设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            List<DataMinute> lists = this.wrapperDataQuery(startTime, endTime, device);
            dataMinutes.add(lists);
            // 设备名称
            Device dd = deviceService.findDeviceNo(device);
            deviceNames.add(dd.getName());
        }

        try {
            HSSFWorkbook excelToDataHistory = ExportExcelUtil.createExcelToDataMinute(dataMinutes, deviceNames);

            String title = "分钟数据表格";
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
            log.info("导出分钟数据Excel表格异常");
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
    private List<DataMinute> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<DataMinute> dataMinutes = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, ConstUtil.MINUTE_TB);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<DataMinute> history = dataMinuteService.queryDataMinuteByDate(startDate, endDate, deviceNo);
            dataMinutes.addAll(history);
        }
        return dataMinutes;
    }
}
