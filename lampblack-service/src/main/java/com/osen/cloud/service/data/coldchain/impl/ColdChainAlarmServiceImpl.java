package com.osen.cloud.service.data.coldchain.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.model.coldchain.ColdChainAlarmMapper;
import com.osen.cloud.service.data.coldchain.ColdChainAlarmService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
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

    @Override
    public Map<String, Object> getAlarmHistory(Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String start = (String) params.get("startTime");
        String end = (String) params.get("endTime");
        long pageNumber = (long) params.get("pageNumber");
        // 日期时间格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        // 分页
        Page<ColdChainAlarm> coldChainAlarmPage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
        // 条件封装
        LambdaQueryWrapper<ColdChainAlarm> queryWrapper = Wrappers.<ColdChainAlarm>lambdaQuery();
        queryWrapper.eq(ColdChainAlarm::getDeviceNo, deviceNo);
        queryWrapper.between(ColdChainAlarm::getDateTime, startTime, endTime);
        queryWrapper.orderByAsc(ColdChainAlarm::getDateTime);
        // 查询
        IPage<ColdChainAlarm> coldChainAlarmIPage = super.page(coldChainAlarmPage, queryWrapper);
        // 结果封装
        Map<String, Object> resultMap = new HashMap<>(0);
        if (coldChainAlarmIPage.getTotal() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("alarmHistory", null);
        } else {
            resultMap.put("total", coldChainAlarmIPage.getTotal());
            resultMap.put("alarmHistory", coldChainAlarmIPage.getRecords());
        }
        return resultMap;
    }
}
