package com.osen.cloud.service.user_device;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.UserDevice;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:20
 * Description: 用户设备服务接口
 */
public interface UserDeviceService extends IService<UserDevice> {

    /**
     * 取消指定用户与设备关联
     *
     * @param params 参数
     * @return 信息
     */
    boolean channelUserToDevice(Map<String, String> params);

    /**
     * 添加指定用户与设备关联
     *
     * @param params 参数
     * @return 信息
     */
    boolean addUserToDevice(Map<String, String> params);
}
