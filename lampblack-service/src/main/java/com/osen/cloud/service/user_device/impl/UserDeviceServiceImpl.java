package com.osen.cloud.service.user_device.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.UserDevice;
import com.osen.cloud.model.user_device.UserDeviceMapper;
import com.osen.cloud.service.user_device.UserDeviceService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:21
 * Description: 用户设备服务接口实现类
 */
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements UserDeviceService {
}
