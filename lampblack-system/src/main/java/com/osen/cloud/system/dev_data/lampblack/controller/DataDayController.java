package com.osen.cloud.system.dev_data.lampblack.controller;

import com.osen.cloud.common.entity.dev_lampblack.DataDay;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.lampblack.DataDayService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.lampblack.util.ExportExcelUtil;
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
 * Description: 设备天数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataDayController {

    @Autowired
    private DataDayService dataDayService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 查询天数据历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/day/queryByDate")
    public RestResult queryDataHistoryByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<DataDay> dataDays = this.wrapperDataQuery(startTime, endTime, deviceNo, ConstUtil.HistoryType);
        return RestResultUtil.success(dataDays);
    }

    /**
     * 导出分钟数据到Excel表格
     *
     * @param params   参数
     * @param response 参数
     */
    @PostMapping("/data/day/exportToExcel")
    public void exportDataDayToExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<DataDay>> dataDays = new ArrayList<>(0);
        // 查询设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            List<DataDay> lists = this.wrapperDataQuery(startTime, endTime, device, ConstUtil.ExportType);
            dataDays.add(lists);
            // 设备名称
            Device dd = deviceService.findDeviceNo(device);
            deviceNames.add(dd.getName());
        }

        try {
            HSSFWorkbook excelToDataHistory = ExportExcelUtil.createExcelToDataDay(dataDays, deviceNames);

            String title = "天数据表格";
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
            log.info("导出天数据Excel表格异常");
            log.info(e.getMessage());
        }
    }

    /**
     * 查询指定设备的范围日期查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param deviceNo  设备号
     * @param type      1表示历史查询，2表示数据导出
     * @return 信息
     */
    private List<DataDay> wrapperDataQuery(String startTime, String endTime, String deviceNo, int type) {
        List<DataDay> dataDays = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, ConstUtil.DAY_TB);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime( MonthCode.Lampblack.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<DataDay> history = dataDayService.queryDataDayByDate(startDate, endDate, deviceNo, type);
            dataDays.addAll(history);
        }
        return dataDays;
    }
}
