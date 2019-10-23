package com.osen.cloud.service.data.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.lampblack.AlarmRealtime;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.data.AlarmRealtimeMapper;
import com.osen.cloud.service.data.AlarmRealtimeService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备实时报警记录服务接口实现类
 */
@Service
public class AlarmRealtimeServiceImpl extends ServiceImpl<AlarmRealtimeMapper, AlarmRealtime> implements AlarmRealtimeService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<AlarmRealtime> findAlarmTOAccount(String account) {
        List<AlarmRealtime> alarmRealtimes = new ArrayList<>(0);
        // 查询用户关联设备
        Map<String, Object> deviceToUser = deviceService.finaAllDeviceToUser(account);
        List<Device> devices = (List<Device>) deviceToUser.get("devices");
        if (devices == null || devices.size() == 0)
            return alarmRealtimes;
        for (Device device : devices) {
            boolean isExist = stringRedisTemplate.boundHashOps(ConstUtil.ALARM_KEY).hasKey(device.getDeviceNo());
            // 该设备存在报警
            if (isExist) {
                String resJson = (String) stringRedisTemplate.boundHashOps(ConstUtil.ALARM_KEY).get(device.getDeviceNo());
                AlarmRealtime alarmRealtime = JSON.parseObject(resJson, AlarmRealtime.class);
                alarmRealtimes.add(alarmRealtime);
            }
        }
        return alarmRealtimes;
    }
}
