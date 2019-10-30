package com.osen.cloud.system.system_device.controller;

import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
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
    @PostMapping("/device/toUserAccount/{type}")
    public RestResult findDeviceByUserAccount(@RequestBody Map<String, Object> params, @PathVariable("type") String type) {
        Map<String, Object> deviceByUserAccount = deviceService.findDeviceByUserAccount(params,type);
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
     * 超级管理员权限
     *
     * @param device 设备
     * @return 信息
     */
    @PostMapping("/device/add")
    public RestResult addDevice(@RequestBody Device device) {
        boolean addDevice = deviceService.addDevice(device, SecurityUtil.getUserId());
        if (addDevice)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }

    /**
     * 统计查询指定用户设备状态
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/device/statusCount/{account}")
    public RestResult findDeviceStatusToUser(@PathVariable("account") String account) {
        Map<String, Integer> deviceStatusToUser = deviceService.findDeviceStatusToUser(account);
        return RestResultUtil.success(deviceStatusToUser);
    }

    /**
     * 根据设备号查询指定设备信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/device/get/{deviceNo}")
    public RestResult findDeviceToNo(@PathVariable("deviceNo") String deviceNo) {
        Device device = deviceService.findDeviceNo(deviceNo);
        if (device != null)
            return RestResultUtil.success(device);
        else
            return RestResultUtil.failed();
    }

    /**
     * 查询指定用户下的所有设备列表信息
     * 不分页
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/device/getAll/{account}")
    public RestResult finaAllDeviceToUser(@PathVariable("account") String account) {
        Map<String, Object> allDeviceToUser = deviceService.finaAllDeviceToUser(account);
        return RestResultUtil.success(allDeviceToUser);
    }

    /**
     * 修改设备信息
     *
     * @param device 设备
     * @return 信息
     */
    @PostMapping("/device/update")
    public RestResult updateDevice(@RequestBody Device device) {
        boolean deviceInfo = deviceService.updateDeviceInfo(device);
        if (deviceInfo)
            return RestResultUtil.success("修改设备成功");
        else
            return RestResultUtil.error(ConstUtil.UNOK, "修改设备失败");
    }
}
