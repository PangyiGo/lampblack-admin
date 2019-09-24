package com.osen.cloud.system.device.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
