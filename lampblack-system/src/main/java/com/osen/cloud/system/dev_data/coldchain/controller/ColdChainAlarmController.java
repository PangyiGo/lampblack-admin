package com.osen.cloud.system.dev_data.coldchain.controller;

import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.coldchain.ColdChainAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-04
 * Time: 9:20
 * Description: 冷链实时报警控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainAlarmController {

    @Autowired
    private ColdChainAlarmService coldChainAlarmService;

    /**
     * 根据指定账号获取设备实时报警数据
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/coldchain/alarm/{account}")
    public RestResult getAlarmRealtime(@PathVariable("account") String account) {
        List<ColdChainAlarm> realtimeAlarm = coldChainAlarmService.getRealtimeAlarm(account);
        return RestResultUtil.success(realtimeAlarm);
    }
}
