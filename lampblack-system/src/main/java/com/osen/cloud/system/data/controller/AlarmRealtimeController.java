package com.osen.cloud.system.data.controller;

import com.osen.cloud.common.entity.lampblack.AlarmRealtime;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.AlarmRealtimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:06
 * Description: 设备报警实时记录控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AlarmRealtimeController {

    @Autowired
    private AlarmRealtimeService alarmRealtimeService;

    /**
     * 查询指定用户实时报警数据
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/alarm/realtime/{account}")
    public RestResult findAlarmTOAccount(@PathVariable("account") String account) {
        List<AlarmRealtime> alarmTOAccount = alarmRealtimeService.findAlarmTOAccount(account);
        return RestResultUtil.success(alarmTOAccount);
    }
}
