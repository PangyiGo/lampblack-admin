package com.osen.cloud.service.data.coldchain.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.model.coldchain.ColdChainAlarmMapper;
import com.osen.cloud.service.data.coldchain.ColdChainAlarmService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 15:14
 * Description: 冷链实时数据通用服务接口实现类
 */
@Service
public class ColdChainAlarmServiceImpl extends ServiceImpl<ColdChainAlarmMapper, ColdChainAlarm> implements ColdChainAlarmService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAlarm(ColdChainAlarm coldChainAlarm) {
        super.save(coldChainAlarm);
    }

    @Override
    public List<ColdChainAlarm> getRealtimeAlarm(String account) {
        List<ColdChainAlarm> coldChainAlarms = new ArrayList<>(0);
        Map<String, Object> device = deviceService.finaAllDeviceToUser(account);
        List<Device> deviceList = (List<Device>) device.get("devices");
        // 无关联设备
        if (deviceList.size() == 0)
            return coldChainAlarms;
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(TableUtil.Cold_Alarm);
        for (Device dev : deviceList) {
            // 是否存在报警
            Boolean hasKey = operations.hasKey(dev.getDeviceNo());
            if (hasKey) {
                String json = (String) operations.get(dev.getDeviceNo());
                ColdChainAlarm coldChainAlarm = JSON.parseObject(json, ColdChainAlarm.class);
                coldChainAlarms.add(coldChainAlarm);
            }
        }
        return coldChainAlarms;
    }
}
