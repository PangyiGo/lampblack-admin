package com.osen.cloud.system.dev_data.vocs.controller;

import com.osen.cloud.common.entity.dev_vocs.VocDay;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.vocs.VocDayService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.vocs.util.ExportExcelUtil;
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
 * Date: 2019-10-31
 * Time: 15:36
 * Description: VOC设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class VocDayController {

    @Autowired
    private VocDayService vocDayService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 数据日期范围查询历史数据
     * 查询封装
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param deviceNo  设备号
     * @return 信息
     */
    private List<VocDay> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<VocDay> vocDays = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.VocDay);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.Voc.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<VocDay> vocDayList = vocDayService.queryHistoryByDate(startDate, endDate, deviceNo);
            vocDays.addAll(vocDayList);
        }
        return vocDays;
    }

    /**
     * 获取设备历史数据
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/voc/day/query")
    public RestResult queryHistoryByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<VocDay> vocDays = this.wrapperDataQuery(startTime, endTime, deviceNo);
        return RestResultUtil.success(vocDays);
    }

    /**
     * 数据导出到excel表格
     *
     * @param params   参数
     * @param response 响应
     */
    @PostMapping("/voc/day/export")
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<VocDay>> vocHistories = new ArrayList<>(0); // 批量设备数据
        // 设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            // 数据
            List<VocDay> vocDayList = this.wrapperDataQuery(startTime, endTime, device);
            vocHistories.add(vocDayList);
            // 设备名称
            Device dev = deviceService.findDeviceNo(device);
            deviceNames.add(dev.getName());
        }

        // Excel表格导出
        try {
            HSSFWorkbook hssfWorkbook = ExportExcelUtil.dayExcelExport(vocHistories, deviceNames);

            String title = "VOC天数据表格";
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
            log.error("导出VOC天数据Excel表格异常");
            log.error(e.getMessage());
        }
    }
}