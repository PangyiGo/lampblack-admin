package com.osen.cloud.system.data.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.AlarmHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:06
 * Description: 设备报警记录控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AlarmHistoryController {

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    /**
     * 查询指定设备报警历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/alarm/history")
    public RestResult queryAlarmHistoryByAccount(@RequestBody Map<String, Object> params) {
        Map<String, Object> historyByAccount = alarmHistoryService.queryAlarmHistoryByAccount(params);
        return RestResultUtil.success(historyByAccount);
    }
}
