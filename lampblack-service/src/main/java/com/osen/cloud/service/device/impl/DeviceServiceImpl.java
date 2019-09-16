package com.osen.cloud.service.device.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.Device;
import com.osen.cloud.common.entity.UserDevice;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.model.device.DeviceMapper;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.service.user_device.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.osen.cloud.common.enums.InfoMessage.InsertDevice_Error;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:06
 * Description: 设备服务接口实现类
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private UserDeviceService userDeviceService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDevice(Device device, Integer userId) {
        if (this.findDeviceNo(device.getDeviceNo()) != null)
            throw new ServiceException(InsertDevice_Error.getCode(), InsertDevice_Error.getMessage());
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        if (super.save(device)) {
            Integer deviceID = device.getId();
            UserDevice userDevice = new UserDevice();
            userDevice.setDeviceId(deviceID);
            userDevice.setUserId(userId);
            return userDeviceService.save(userDevice);
        }
        return false;
    }

    @Override
    public Device findDeviceNo(String deviceNo) {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery().eq(Device::getDeviceNo, deviceNo);
        return super.getOne(wrapper, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDeviceStatus(Integer isLive, String deviceNo) {
        LambdaUpdateWrapper<Device> updateWrapper = Wrappers.<Device>lambdaUpdate().set(Device::getIsLive, isLive).eq(Device::getDeviceNo, deviceNo).set(Device::getUpdateTime, LocalDateTime.now());
        return super.update(updateWrapper);
    }

}
