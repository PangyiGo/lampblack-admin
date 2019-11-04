package com.osen.cloud.service.data.vocs.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocAlarm;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.model.vos.VocAlarmMapper;
import com.osen.cloud.service.data.vocs.VocAlarmService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:45
 * Description: VOC实时数据服务接口实现类
 */
@Service
public class VocAlarmServiceImpl extends ServiceImpl<VocAlarmMapper, VocAlarm> implements VocAlarmService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DeviceService deviceService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAlarm(VocAlarm vocAlarm) {
        super.save(vocAlarm);
    }

    @Override
    public List<VocAlarm> getRealtimeAlarm(String account) {
        List<VocAlarm> vocAlarms = new ArrayList<>(0);
        List<Device> devices = (List<Device>) deviceService.finaAllDeviceToUser(account).get("devices");
        if (devices == null || devices.size() == 0)
            return vocAlarms;
        for (Device device : devices) {
            Boolean hasKey = stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).hasKey(device.getDeviceNo());
            if (hasKey) {
                String json = (String) stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).get(device.getDeviceNo());
                VocAlarm vocAlarm = JSON.parseObject(json, VocAlarm.class);
                vocAlarms.add(vocAlarm);
            }
        }
        return vocAlarms;
    }
}
