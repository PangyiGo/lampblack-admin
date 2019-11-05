package com.osen.cloud.system.dev_data.vocs.controller;

import com.osen.cloud.common.entity.dev_vocs.VocAlarm;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.vocs.VocAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-11-04
 * Time: 9:41
 * Description:  VOC设备实时报警控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class VocAlarmController {

    @Autowired
    private VocAlarmService vocAlarmService;

    /**
     * 根据账号获取用户设备报警实时数据
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/voc/alarm/{account}")
    public RestResult getRealtimeAlarm(@PathVariable("account") String account) {
        List<VocAlarm> realtimeAlarm = vocAlarmService.getRealtimeAlarm(account);
        return RestResultUtil.success(realtimeAlarm);
    }

    /**
     * 查询指定设备报警历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/voc/alarm/history")
    public RestResult getAlarmHistory(@RequestBody Map<String, Object> params) {
        Map<String, Object> historyByAccount = vocAlarmService.getAlarmHistory(params);
        return RestResultUtil.success(historyByAccount);
    }

}
