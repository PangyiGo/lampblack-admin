package com.osen.cloud.service.device.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.entity.system_user.User;
import com.osen.cloud.common.entity.system_user.UserDevice;
import com.osen.cloud.common.enums.DeviceType;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.model.device.DeviceMapper;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.service.user_device.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDevice(Device device, Integer userId) {
        if (this.findDeviceNo(device.getDeviceNo()) != null)
            throw new ServiceException(InsertDevice_Error.getCode(), InsertDevice_Error.getMessage());
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        if (super.save(device)) {
            // 判断设备类型，冷链设备添加监控点
            if (device.getType().equals(DeviceType.ColdChain.getName())) {
                coldChainMonitorService.insertDefaultMonitor(device.getDeviceNo());
            }
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

    @Override
    public Map<String, Object> findDeviceByUserAccount(Map<String, Object> params, String type) {
        Map<String, Object> resultMap = new HashMap<>(0);
        List<Device> devices = new ArrayList<>(0);
        // 当前登录用户账号
        String username = SecurityUtil.getUsername();
        // 获取指定用户
        User user = userService.findByUsername(username);
        if (user == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定用户信息");
        // 获取用户设备关联列表
        int pageNumber = (int) params.get("pageNumber");
        LambdaQueryWrapper<UserDevice> userDeviceLambdaQueryWrapper = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, user.getId());
        List<UserDevice> userDevices = userDeviceService.list(userDeviceLambdaQueryWrapper);
        // 总记录数
        long total = 0;
        if (userDevices != null && userDevices.size() > 0) {
            LambdaQueryWrapper<Device> deviceLambdaQueryWrapper = Wrappers.<Device>lambdaQuery();
            // 查询设备类型
            deviceLambdaQueryWrapper.eq(Device::getType, type);
            // 获取指定用户设备列表
            List<Integer> deviceId = new ArrayList<>();
            for (UserDevice userDevice : userDevices) {
                deviceId.add(userDevice.getDeviceId());
            }
            // 查询设备列表
            deviceLambdaQueryWrapper.in(Device::getId, deviceId);
            // 设备号
            String deviceNo = (String) params.get("deviceNo");
            if (StringUtils.isNotEmpty(deviceNo))
                deviceLambdaQueryWrapper.and(query -> query.like(Device::getDeviceNo, deviceNo).or().like(Device::getName, deviceNo).or().like(Device::getProvince, deviceNo).or().like(Device::getCity, deviceNo).or().like(Device::getArea, deviceNo).or().like(Device::getAddress, deviceNo));
            // 分页查询
            Page<Device> devicePage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
            IPage<Device> deviceIPage = super.page(devicePage, deviceLambdaQueryWrapper);
            total = deviceIPage.getTotal();
            devices = deviceIPage.getRecords();
        }
        // 封装数据
        resultMap.put("total", total);
        resultMap.put("devices", devices);
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllDeviceToSystem(Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>(0);
        // 根据公司名称模糊查询
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery();
        String name = (String) params.get("deviceName");
        if (StringUtils.isNotEmpty(name))
            wrapper.like(Device::getName, name).or().like(Device::getDeviceNo, name).or().like(Device::getAddress, name);
        // 时间升序排列
        wrapper.orderByAsc(Device::getCreateTime);
        // 分页查询
        int pageNumber = (int) params.get("pageNumber");
        Page<Device> devicePage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER + 2);
        IPage<Device> deviceIPage = super.page(devicePage, wrapper);
        // 总记录数
        long total = deviceIPage.getTotal();
        // 总记录
        List<Device> devices = deviceIPage.getRecords();
        // 封装数据
        resultMap.put("total", total);
        resultMap.put("devices", devices);
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteToDeviceNo(String deviceNo) {
        Map<String, Object> resultMap = new HashMap<>(0);
        // 查询指定设备
        LambdaQueryWrapper<Device> queryWrapper = Wrappers.<Device>lambdaQuery().eq(Device::getDeviceNo, deviceNo);
        Device selectOne = baseMapper.selectOne(queryWrapper);
        if (selectOne == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定设备信息");
        // 查询用户与设备关联列表
        LambdaQueryWrapper<UserDevice> deviceLambdaQueryWrapper = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getDeviceId, selectOne.getId());
        List<UserDevice> userToDevice = userDeviceService.findUserToDevice(deviceLambdaQueryWrapper);
        if ((userToDevice == null)) {
            // 除了超级管理员，无其他用户关联，执行删除设备
            super.removeById(selectOne.getId());
            resultMap.put("tips", "成功删除 " + selectOne.getDeviceNo() + " 设备");
            resultMap.put("code", ConstUtil.OK);
        } else if (userToDevice.size() == 1 && SecurityUtil.getUserId().equals(userToDevice.get(0).getUserId())) {
            // 解除超级管理员与设备绑定
            Map<String, String> map = new HashMap<>();
            map.put("account", SecurityUtil.getUsername());
            map.put("deviceNo", deviceNo);
            if (userDeviceService.channelUserToDevice(map)) {
                super.removeById(selectOne.getId());
                resultMap.put("tips", "成功删除 " + selectOne.getDeviceNo() + " 设备");
                resultMap.put("code", ConstUtil.OK);
            }
        } else {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("设备：").append(selectOne.getDeviceNo()).append(" 与以下账号已关联");
            stringBuffer.append("[ ");
            // 已有用户与该设备关联
            for (UserDevice userDevice : userToDevice) {
                User user = userService.getById(userDevice.getUserId());
                stringBuffer.append(user.getAccount()).append(" , ");
            }
            stringBuffer.append(" ]");
            resultMap.put("tips", stringBuffer.toString());
            resultMap.put("code", ConstUtil.UNOK);
        }
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDevice(Device device) {
        if (this.findDeviceNo(device.getDeviceNo()) != null)
            throw new ServiceException(InsertDevice_Error.getCode(), InsertDevice_Error.getMessage());
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        // 默认不在线
        device.setIsLive(ConstUtil.CLOSE_STATUS);
        return super.save(device);
    }

    @Override
    public Map<String, Integer> findDeviceStatusToUser(String account, String type) {
        // 获取指定用户
        User user = userService.findByUsername(account);
        if (user == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定用户信息");
        Map<String, Integer> resultMap = new HashMap<>(0);
        // 查询指定用户设备关联
        LambdaQueryWrapper<UserDevice> deviceQuery = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, user.getId());
        List<UserDevice> userToDevice = userDeviceService.findUserToDevice(deviceQuery);
        List<Integer> deviceIds = new ArrayList<>(0);
        // 无关联设备
        if (userToDevice == null || userToDevice.size() <= 0) {
            resultMap.put("online", 0);
            resultMap.put("offline", 0);
            return resultMap;
        }
        for (UserDevice userDevice : userToDevice) {
            deviceIds.add(userDevice.getDeviceId());
        }
        // 查询在线设备
        LambdaQueryWrapper<Device> onlineQuery = Wrappers.<Device>lambdaQuery();
        onlineQuery.eq(Device::getType, type).eq(Device::getIsLive, ConstUtil.OPEN_STATUS).in(Device::getId, deviceIds);
        int online = super.count(onlineQuery);
        resultMap.put("online", online);
        // 查询离线设备
        LambdaQueryWrapper<Device> offlineQuery = Wrappers.<Device>lambdaQuery();
        offlineQuery.eq(Device::getType, type).eq(Device::getIsLive, ConstUtil.CLOSE_STATUS).in(Device::getId, deviceIds);
        int offline = super.count(offlineQuery);
        resultMap.put("offline", offline);
        // 返回
        return resultMap;
    }

    @Override
    public Map<String, Object> finaAllDeviceToUser(String account) {
        List<Device> devices = new ArrayList<>(0);
        Map<String, Object> resultMap = new HashMap<>(0);
        // 获取指定用户
        User user = userService.findByUsername(account);
        if (user == null)
            throw new ServiceException(ConstUtil.UNOK, "无法查询指定用户信息");
        // 查询指定用户设备关联
        LambdaQueryWrapper<UserDevice> deviceQuery = Wrappers.<UserDevice>lambdaQuery().eq(UserDevice::getUserId, user.getId());
        List<UserDevice> userToDevice = userDeviceService.findUserToDevice(deviceQuery);
        if (userToDevice == null || userToDevice.size() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("devices", devices);
            return resultMap;
        } else {
            List<Integer> deviceIds = new ArrayList<>(0);
            for (UserDevice userDevice : userToDevice) {
                deviceIds.add(userDevice.getDeviceId());
            }
            devices = (List<Device>) super.listByIds(deviceIds);
            int total = devices.size();
            resultMap.put("total", total);
            resultMap.put("devices", devices);
            return resultMap;
        }
    }

    @Override
    public boolean updateDeviceInfo(Device device) {
        device.setUpdateTime(LocalDateTime.now());
        LambdaUpdateWrapper<Device> wrapper = Wrappers.<Device>lambdaUpdate().eq(Device::getDeviceNo, device.getDeviceNo());
        return this.update(device, wrapper);
    }

    @Override
    public List<Device> findAllToMap(String type) {
        List<Device> deviceList = new ArrayList<>(0);
        List<Integer> deviceIds = userDeviceService.findDeviceIdsByUsername();
        if (deviceIds.size() == 0)
            return deviceList;
        LambdaQueryWrapper<Device> query = Wrappers.<Device>lambdaQuery().in(Device::getId, deviceIds).eq(Device::getType, type);
        try {
            deviceList = super.list(query);
        } catch (Exception e) {
            return deviceList;
        }
        return deviceList;
    }

}
