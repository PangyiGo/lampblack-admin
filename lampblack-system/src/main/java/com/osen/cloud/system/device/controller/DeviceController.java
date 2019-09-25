package com.osen.cloud.system.device.controller;

import com.osen.cloud.common.entity.Device;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-24
 * Time: 17:19
 * Description: 设备服务控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 分页查询指定用户设备列表信息
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/device/toUserAccount")
    public RestResult findDeviceByUserAccount(@RequestBody Map<String, Object> params) {
        Map<String, Object> deviceByUserAccount = deviceService.findDeviceByUserAccount(params);
        return RestResultUtil.success(deviceByUserAccount);
    }

    /**
     * 分页查询系统所有设备列表信息
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/device/get/all")
    public RestResult findAllDeviceToSystem(@RequestBody Map<String, Object> params) {
        Map<String, Object> allDeviceToSystem = deviceService.findAllDeviceToSystem(params);
        return RestResultUtil.success(allDeviceToSystem);
    }

    /**
     * 删除指定设备号的设备
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/device/delete/{deviceNo}")
    public RestResult deleteToDeviceNo(@PathVariable("deviceNo") String deviceNo) {
        Map<String, Object> toDeviceNo = deviceService.deleteToDeviceNo(deviceNo);
        return RestResultUtil.success(toDeviceNo);
    }

    /**
     * 添加设备
     *
     * @param device 设备
     * @return 信息
     */
    @PostMapping("/device/add")
    public RestResult addDevice(@RequestBody Device device) {
        boolean addDevice = deviceService.addDevice(device);
        if (addDevice)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }
}
