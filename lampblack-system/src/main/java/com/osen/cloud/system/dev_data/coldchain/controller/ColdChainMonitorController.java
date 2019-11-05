package com.osen.cloud.system.dev_data.coldchain.controller;

import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 11:07
 * Description: 冷链设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainMonitorController {

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    /**
     * 获取指定设备号的监控点数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/coldchain/monitor/{deviceNo}")
    public RestResult getRealtime(@PathVariable("deviceNo") String deviceNo) {
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        if (monitor != null) {
            return RestResultUtil.success(monitor);
        } else {
            monitor.setM01("未定义#1");
            monitor.setM02("未定义#2");
            monitor.setM03("未定义#3");
            monitor.setM03("未定义#4");
        }
        return RestResultUtil.success(monitor);
    }

}
