package com.osen.cloud.service.data.vocs.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.model.vos.VocHistoryMapper;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import com.osen.cloud.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:45
 * Description: VOC实时数据服务接口实现类
 */
@Service
public class VocHistoryServiceImpl extends ServiceImpl<VocHistoryMapper, VocHistory> implements VocHistoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DeviceService deviceService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHistory(VocHistory vocHistory) {
        super.save(vocHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public VocHistory getRealtime(String deviceNo) {
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(TableUtil.Voc_RealTime);
        Boolean key = operations.hasKey(deviceNo);
        if (key) {
            String dataJson = (String) operations.get(deviceNo);
            if (StrUtil.isNotEmpty(dataJson))
                return JSON.parseObject(dataJson, VocHistory.class);
        }
        return null;
    }

    @Override
    public List<VocHistory> getDataToday(String args, String deviceNo) {
        List<VocHistory> vocHistories = new ArrayList<>(0);
        // 当前时间
        LocalDateTime nowToday = LocalDateTime.now();
        // 开始时间
        LocalDateTime start = LocalDateTime.of(nowToday.getYear(), nowToday.getMonthValue(), nowToday.getDayOfMonth(), 0, 0, 0, 0);
        LambdaQueryWrapper<VocHistory> query = Wrappers.<VocHistory>lambdaQuery();
        switch (args) {
            case "voc":
                query.select(VocHistory::getVoc, VocHistory::getDateTime);
                break;
            case "flow":
                query.select(VocHistory::getFlow, VocHistory::getDateTime);
                break;
            case "speed":
                query.select(VocHistory::getSpeed, VocHistory::getDateTime);
                break;
            case "pressure":
                query.select(VocHistory::getPressure, VocHistory::getDateTime);
                break;
            case "temp":
                query.select(VocHistory::getTemp, VocHistory::getDateTime);
                break;
        }
        query.eq(VocHistory::getDeviceNo, deviceNo);
        query.between(VocHistory::getDateTime, start, nowToday).orderByAsc(VocHistory::getDateTime);
        try {
            vocHistories = super.list(query);
        } catch (Exception e) {
            return vocHistories;
        }
        return vocHistories;
    }

    @Override
    public List<VocHistory> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<VocHistory> vocHistories = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<VocHistory> query = Wrappers.<VocHistory>lambdaQuery();
        query.eq(VocHistory::getDeviceNo, deviceNo);
        query.between(VocHistory::getDateTime, start, end).orderByAsc(VocHistory::getDateTime);
        try {
            vocHistories = super.list(query);
        } catch (Exception e) {
            return vocHistories;
        }
        return vocHistories;
    }

    @Override
    public List<VocHistory> getRealtimeToUser() {
        List<VocHistory> vocHistories = new ArrayList<>(0);
        String username = SecurityUtil.getUsername();
        List<Device> devices = (List<Device>) deviceService.finaAllDeviceToUser(username).get("devices");
        for (Device device : devices) {
            Boolean hasKey = stringRedisTemplate.boundHashOps(TableUtil.Voc_RealTime).hasKey(device.getDeviceNo());
            if (hasKey) {
                String json = (String) stringRedisTemplate.boundHashOps(TableUtil.Voc_RealTime).get(device.getDeviceNo());
                VocHistory vocHistory = JSON.parseObject(json, VocHistory.class);
                vocHistories.add(vocHistory);
            }
        }
        return vocHistories;
    }

    @Override
    public List<VocHistory> getVocHistory(String args, String deviceNo) {
        List<VocHistory> vocHistories = new ArrayList<>(0);
        // 当前时间
        LocalDateTime end = LocalDateTime.now();
        // 开始时间
        LocalDateTime start = end.minusHours(12);
        LambdaQueryWrapper<VocHistory> query = Wrappers.<VocHistory>lambdaQuery();
        switch (args) {
            case "voc":
                query.select(VocHistory::getVoc, VocHistory::getDateTime);
                break;
            case "flow":
                query.select(VocHistory::getFlow, VocHistory::getDateTime);
                break;
            case "speed":
                query.select(VocHistory::getSpeed, VocHistory::getDateTime);
                break;
            case "pressure":
                query.select(VocHistory::getPressure, VocHistory::getDateTime);
                break;
            case "temp":
                query.select(VocHistory::getTemp, VocHistory::getDateTime);
                break;
        }
        query.eq(VocHistory::getDeviceNo, deviceNo);
        query.between(VocHistory::getDateTime, start, end).orderByAsc(VocHistory::getDateTime);
        try {
            vocHistories = super.list(query);
        } catch (Exception e) {
            return vocHistories;
        }
        return vocHistories;
    }

    @Override
    public VocHistory getOneData(String deviceNo, LocalDateTime dateTime) {
        LambdaQueryWrapper<VocHistory> wrapper = Wrappers.<VocHistory>lambdaQuery().select(VocHistory::getDeviceNo).eq(VocHistory::getDeviceNo, deviceNo).eq(VocHistory::getDateTime, dateTime);
        try {
            return super.getOne(wrapper, true);
        } catch (Exception e) {
            return null;
        }
    }
}
