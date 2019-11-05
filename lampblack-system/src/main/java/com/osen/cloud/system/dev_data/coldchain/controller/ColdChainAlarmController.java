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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            realTimeVOS.add(realTimeVO);
        }
        return RestResultUtil.success(realTimeVOS);
    }

    /**
     * 分页查询指定设备的报警记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/coldchain/alarm/history")
    public RestResult getAlarmHistory(@RequestBody Map<String, Object> params) {
        // 数据表
        List<RealTimeVO> realTimeVOS = new ArrayList<>(0);
        Map<String, Object> alarmHistory = coldChainAlarmService.getAlarmHistory(params);
        List<ColdChainAlarm> coldChainAlarms = (List<ColdChainAlarm>) alarmHistory.get("alarmHistory");
        long total = (long) alarmHistory.get("total");
        // 结果
        Map<String, Object> map = new HashMap<>(0);
        if (coldChainAlarms == null) {
            map.put("alarmHistory", realTimeVOS);
            map.put("total", 0);
            return RestResultUtil.success(map);
        }
        for (ColdChainAlarm coldChainAlarm : coldChainAlarms) {
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
            realTimeVOS.add(realTimeVO);
        }
        map.put("alarmHistory", realTimeVOS);
        map.put("total", total);
        return RestResultUtil.success(map);
    }
}
