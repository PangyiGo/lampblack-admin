package com.osen.cloud.system.dev_data.lampblack.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_lampblack.DataHistory;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.lampblack.DataHistoryService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.dev_data.lampblack.util.DataHistoryRealVO;
import com.osen.cloud.system.dev_data.lampblack.util.ExportExcelUtil;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
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
 * Date: 2019-09-26
 * Time: 10:06
 * Description: 实时数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataHistoryController {

    @Autowired
    private DataHistoryService dataHistoryService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 根据设备号实时查询指定设备
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/data/realtime/{deviceNo}")
    public RestResult returnRealtimeData(@PathVariable("deviceNo") String deviceNo) {
        DataHistory dataHistory = dataHistoryService.returnRealtimeData(deviceNo);
        return RestResultUtil.success(dataHistory);
    }

    /**
     * 批量查询指定设备号列表实时数据
     *
     * @param params 设备号列表
     * @return 信息
     */
    @PostMapping("/data/realtime/batch")
    public RestResult batchFindDataToDeviceNo() {
        List<DataHistory> dataHistoryList = dataHistoryService.batchFindDataToDeviceNo();
        return RestResultUtil.success(dataHistoryList);
    }

    /**
     * 查询实时数据历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/realtime/queryByDate")
    public RestResult queryDataHistoryByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<DataHistory> dataHistoryList = this.wrapperDataQuery(startTime, endTime, deviceNo);
        return RestResultUtil.success(dataHistoryList);
    }

    /**
     * 导出实时数据到Excel表格
     *
     * @param params   参数
     * @param response 参数
     */
    @PostMapping("/data/realtime/exportToExcel")
    public void exportDataHistoryToExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<DataHistory>> dataHistories = new ArrayList<>(0);
        // 查询设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            List<DataHistory> lists = this.wrapperDataQuery(startTime, endTime, device);
            dataHistories.add(lists);
            // 设备名称
            Device dd = deviceService.findDeviceNo(device);
            deviceNames.add(dd.getName());
        }

        try {
            HSSFWorkbook excelToDataHistory = ExportExcelUtil.createExcelToDataHistory(dataHistories, deviceNames);

            String title = "实时数据表格";
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
            log.error("导出实时数据Excel表格异常");
            log.error(e.getMessage());
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
    private List<DataHistory> wrapperDataQuery(String startTime, String endTime, String deviceNo) {
        List<DataHistory> dataHistoryList = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, ConstUtil.REALTIME_TB);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.Lampblack.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<DataHistory> history = dataHistoryService.queryDataHistoryByDate(startDate, endDate, deviceNo);
            dataHistoryList.addAll(history);
        }
        return dataHistoryList;
    }

    /**
     * 查询当天设备实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/data/realtime/today/{deviceNo}")
    public RestResult queryDataToDay(@PathVariable("deviceNo") String deviceNo) {
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(ConstUtil.REALTIME_TB));
        List<DataHistory> dataHistoryList = dataHistoryService.queryDataToDay(deviceNo);
        if (dataHistoryList.size() <= 0)
            return RestResultUtil.success(dataHistoryList);
        List<DataHistoryRealVO> dataHistoryRealVOS = new ArrayList<>(0);
        for (DataHistory dataHistory : dataHistoryList) {
            DataHistoryRealVO realVO = new DataHistoryRealVO();
            BeanUtil.copyProperties(dataHistory, realVO);
            dataHistoryRealVOS.add(realVO);
        }
        return RestResultUtil.success(dataHistoryRealVOS);
    }
}
