package com.osen.cloud.service.user_device.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.Device;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.common.entity.UserDevice;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.user_device.UserDeviceMapper;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.service.user_device.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:21
 * Description: 用户设备服务接口实现类
 */
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements UserDeviceService {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean channelUserToDevice(Map<String, String> params) {
        String username = params.get("account");
        String deviceNo = params.get("deviceNo");
        User byUsername = userService.findByUsername(username);
        if (byUsername == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定用户信息");
        Device serviceDeviceNo = deviceService.findDeviceNo(deviceNo);
        if (serviceDeviceNo == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定设备信息");
        LambdaQueryWrapper<UserDevice> wrapper = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, byUsername.getId()).eq(UserDevice::getDeviceId, serviceDeviceNo.getId());
        return super.remove(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUserToDevice(Map<String, String> params) {
        String username = params.get("account");
        String deviceNo = params.get("deviceNo");
        User byUsername = userService.findByUsername(username);
        if (byUsername == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定用户信息");
        Device serviceDeviceNo = deviceService.findDeviceNo(deviceNo);
        if (serviceDeviceNo == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定设备信息");
        LambdaQueryWrapper<UserDevice> queryWrapper = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, byUsername.getId()).eq(UserDevice::getDeviceId, serviceDeviceNo.getId());
        UserDevice device = super.getOne(queryWrapper);
        if (device != null)
            throw new ServiceException(ConstUtil.UNOK, "该用户与设备重复关联");
        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(byUsername.getId());
        userDevice.setDeviceId(serviceDeviceNo.getId());
        return super.save(userDevice);
    }

    @Override
    public List<UserDevice> findUserToDevice(LambdaQueryWrapper<UserDevice> queryWrapper) {
        List<UserDevice> selectList = baseMapper.selectList(queryWrapper);
        if (selectList == null || selectList.size() == 0)
            return null;
        return selectList;
    }
}
