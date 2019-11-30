package com.osen.cloud.system.dev_data.vocs.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.vocs.util.ExportExcelUtil;
import com.osen.cloud.system.dev_data.vocs.util.HistoryVO;
import com.osen.cloud.system.dev_data.vocs.util.TodayVO;
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
 * Time: 15:36
 * Description: VOC设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class VocHistoryController {

    @Autowired
    private VocHistoryService vocHistoryService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/voc/realtime/{deviceNo}")
    public RestResult getRealtime(@PathVariable("deviceNo") String deviceNo) {
        VocHistory realtime = new VocHistory();
        if (realtime != null)
            realtime = vocHistoryService.getRealtime(deviceNo);
        return RestResultUtil.success(realtime);
    }

    /**
     * 获取指定设备号的参数的历史数据
     *
     * @param args     参数
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/voc/today/{args}/{deviceNo}")
    public RestResult getDataToday(@PathVariable("args") String args, @PathVariable("deviceNo") String deviceNo) {
        // 设置表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.VocHistory));
        List<VocHistory> histories = vocHistoryService.getDataToday(args, deviceNo);
        List<TodayVO> todayVOS = new ArrayList<>(0);
        for (VocHistory vocHistory : histories) {
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        startDate = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        endDate = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(), 23, 59, 59);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.VocHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(tableName, MonthCode.Voc.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<VocHistory> vocHistoryList = vocHistoryService.queryHistoryByDate(startDate, endDate, deviceNo);
            vocHistories.addAll(vocHistoryList);
        }
        return vocHistories;
    }

    /**
     * 获取设备历史数据
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/voc/realtime/query")
    public RestResult queryHistoryByDate(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<VocHistory> vocHistories = this.wrapperDataQuery(startTime, endTime, deviceNo);
        List<HistoryVO> historyVOS = new ArrayList<>(0);
        for (VocHistory vocHistory : vocHistories) {
            HistoryVO historyVO = new HistoryVO();
            BeanUtil.copyProperties(vocHistory, historyVO);
            historyVOS.add(historyVO);
        }
        return RestResultUtil.success(historyVOS);
    }

    /**
     * 数据导出到excel表格
     *
     * @param params   参数
     * @param response 响应
     */
    @PostMapping("/voc/realtime/export")
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        // 参数
        List<String> devices = (List<String>) params.get("devices");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<List<VocHistory>> vocHistories = new ArrayList<>(0); // 批量设备数据
        // 设备名称
        List<String> deviceNames = new ArrayList<>(0);
        for (String device : devices) {
            // 数据
            List<VocHistory> vocHistoryList = this.wrapperDataQuery(startTime, endTime, device);
            vocHistories.add(vocHistoryList);
            // 设备名称
            Device dev = deviceService.findDeviceNo(device);
            deviceNames.add(dev.getName());
        }

        // Excel表格导出
        try {
            HSSFWorkbook hssfWorkbook = ExportExcelUtil.historyExcelExport(vocHistories, deviceNames);

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
    @PostMapping("/voc/realtime/batch")
    public RestResult getBatchRealtime() {
        List<VocHistory> realtimeToUser = vocHistoryService.getRealtimeToUser();
        return RestResultUtil.success(realtimeToUser);
    }
}
