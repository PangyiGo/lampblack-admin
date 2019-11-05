package com.osen.cloud.system.dev_data.coldchain.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.coldchain.ColdChainAlarmService;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.system.dev_data.coldchain.util.RealTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    /**
     * 根据指定账号获取设备实时报警数据
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/coldchain/alarm/{account}")
    public RestResult getAlarmRealtime(@PathVariable("account") String account) {
        List<RealTimeVO> realTimeVOS = new ArrayList<>(0);
        List<ColdChainAlarm> realtimeAlarm = coldChainAlarmService.getRealtimeAlarm(account);
        for (ColdChainAlarm coldChainAlarm : realtimeAlarm) {
            RealTimeVO realTimeVO = new RealTimeVO();
            ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(coldChainAlarm.getDeviceNo());
            if (monitor == null) {
                realTimeVO.setM01("未定义#1");
                realTimeVO.setM02("未定义#2");
                realTimeVO.setM03("未定义#3");
                realTimeVO.setM03("未定义#4");
            }
            BeanUtil.copyProperties(coldChainAlarm, realTimeVO);
            BeanUtil.copyProperties(monitor, realTimeVO);
        }
        return RestResultUtil.success(realTimeVOS);
    }
}
