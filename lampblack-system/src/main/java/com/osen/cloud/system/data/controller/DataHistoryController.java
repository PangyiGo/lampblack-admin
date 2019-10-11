package com.osen.cloud.system.data.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.DataHistory;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.DataHistoryService;
import com.osen.cloud.system.data.util.DataHistoryRealVO;
import com.osen.cloud.system.db_config.MybatisPlusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 根据设备号实时查询指定设备
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/data/realtime/{deviceNo}")
    public RestResult returnRealtimeData(@PathVariable("deviceNo") String deviceNo) {
        log.info("实时数据返回");
        DataHistory dataHistory = dataHistoryService.returnRealtimeData(deviceNo);
        return RestResultUtil.success(dataHistory);
    }

    /**
     * 批量查询指定设备号列表实时数据
     *
     * @param params 设备号列表
     * @return 信息
     */
    @PostMapping("/data/realtime/batchFind")
    public RestResult batchFindDataToDeviceNo(@RequestBody Map<String, List<String>> params) {
        List<String> equipmentIDList = params.get("equipmentIDList");
        List<DataHistory> dataHistoryList = dataHistoryService.batchFindDataToDeviceNo(equipmentIDList);
        log.info("批量实时数据返回");
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
        List<DataHistory> dataHistoryList = new ArrayList<>(0);
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, ConstUtil.REALTIME_TB);
        for (String tableName : queryTableName) {
            MybatisPlusConfig.TableName.set(tableName);
            List<DataHistory> history = dataHistoryService.queryDataHistoryByDate(startDate, endDate, deviceNo);
            dataHistoryList.addAll(history);
        }
        return RestResultUtil.success(dataHistoryList);
    }

    /**
     * 查询当天设备实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/data/realtime/today/{deviceNo}")
    public RestResult queryDataToDay(@PathVariable("deviceNo") String deviceNo) {
        MybatisPlusConfig.TableName.set(ConstUtil.REALTIME_TB);
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
