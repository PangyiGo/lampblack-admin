package com.osen.cloud.system.device.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.user_device.UserDeviceService;
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
 * Time: 17:56
 * Description: 用户设备关联服务控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class UserDeviceController {

    @Autowired
    private UserDeviceService userDeviceService;

    /**
     * 取消指定用户与设备关联
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/device/channelUserToDevice")
    public RestResult channelUserToDevice(@RequestBody Map<String, String> params) {
        boolean userToDevice = userDeviceService.channelUserToDevice(params);
        if (userToDevice)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }

    /**
     * 添加指定用户与设备关联
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/device/addUserToDevice")
    public RestResult addUserToDevice(@RequestBody Map<String, String> params) {
        boolean userToDevice = userDeviceService.addUserToDevice(params);
        if (userToDevice)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }
}
