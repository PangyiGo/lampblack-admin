package com.osen.cloud.system.data;

import com.osen.cloud.common.entity.DataHistory;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.DataHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
