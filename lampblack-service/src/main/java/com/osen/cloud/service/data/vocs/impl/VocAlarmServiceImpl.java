package com.osen.cloud.service.data.vocs.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocAlarm;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.model.vos.VocAlarmMapper;
import com.osen.cloud.service.data.vocs.VocAlarmService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> getAlarmHistory(Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String start = (String) params.get("startTime");
        String end = (String) params.get("endTime");
        int pageNumber = (int) params.get("pageNumber");
        // 日期时间格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        // 分页
        Page<VocAlarm> vocAlarmPage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
        // 条件封装
        LambdaQueryWrapper<VocAlarm> queryWrapper = Wrappers.<VocAlarm>lambdaQuery();
        queryWrapper.eq(VocAlarm::getDeviceNo, deviceNo);
        queryWrapper.between(VocAlarm::getDateTime, startTime, endTime);
        queryWrapper.orderByAsc(VocAlarm::getDateTime);
        // 查询
        IPage<VocAlarm> vocAlarmIPage = super.page(vocAlarmPage, queryWrapper);
        // 结果封装
        Map<String, Object> resultMap = new HashMap<>(0);
        if (vocAlarmIPage.getTotal() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("alarmHistory", null);
        } else {
            resultMap.put("total", vocAlarmIPage.getTotal());
            resultMap.put("alarmHistory", vocAlarmIPage.getRecords());
        }
        return resultMap;
    }
}
