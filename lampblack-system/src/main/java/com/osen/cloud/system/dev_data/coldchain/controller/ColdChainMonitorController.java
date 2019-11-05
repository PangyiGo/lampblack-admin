package com.osen.cloud.system.dev_data.coldchain.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.DeviceType;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.service.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private DeviceService deviceService;

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

    /**
     * 更新设备监控点信息
     *
     * @param coldChainMonitor 监控点
     * @return 信息
     */
    @PostMapping("/coldchain/monitor/update")
    public RestResult update(@RequestBody ColdChainMonitor coldChainMonitor) {
        if (StringUtils.isEmpty(coldChainMonitor.getM01()) || StringUtils.isEmpty(coldChainMonitor.getM02()) || StringUtils.isEmpty(coldChainMonitor.getM03()) || StringUtils.isEmpty(coldChainMonitor.getM04()))
            return RestResultUtil.error(ConstUtil.UNOK, "请填写完整监控点，不允许为空值");
        boolean updateMonitor = coldChainMonitorService.updateMonitor(coldChainMonitor);
        if (updateMonitor)
            return RestResultUtil.success("更新监控点成功");
        else
            return RestResultUtil.error(ConstUtil.UNOK, "更新监控点异常，请稍后重试");
    }

    /**
     * 获取当前用户的冷链设备的监控点
     *
     * @return 信息
     */
    @PostMapping("/coldchain/monitor/list")
    public RestResult getAllMonitorToUser(@RequestBody Map<String, Object> params) {
        String username = SecurityUtil.getUsername();
        Map<String, Object> map = deviceService.finaAllDeviceToUser(username);
        // 设备列表
        List<Device> deviceList = (List<Device>) map.get("devices");
        List<String> deviceNos = new ArrayList<>(0);
        for (Device device : deviceList) {
            if (!device.getType().equals(DeviceType.ColdChain.getName()))
                continue;
            deviceNos.add(device.getDeviceNo());
        }
        Map<String, Object> allMonitorToUser = coldChainMonitorService.getAllMonitorToUser(deviceNos, params);
        return RestResultUtil.success(allMonitorToUser);
    }
}
