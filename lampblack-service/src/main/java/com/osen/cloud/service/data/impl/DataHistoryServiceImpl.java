package com.osen.cloud.service.data.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_lampblack.DataHistory;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.data.DataHistoryMapper;
import com.osen.cloud.service.data.DataHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备时候死数据上传服务接口实现类
 */
@Service
public class DataHistoryServiceImpl extends ServiceImpl<DataHistoryMapper, DataHistory> implements DataHistoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertRealtimeData(DataHistory dataHistory) {
        super.save(dataHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public DataHistory returnRealtimeData(String deviceNo) {
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(ConstUtil.DATA_KEY);
        Boolean hasKey = operations.hasKey(deviceNo);
        if (hasKey) {
            String dataJson = (String) operations.get(deviceNo);
            return JSON.parseObject(dataJson, DataHistory.class);
        }
        DataHistory dataHistory = new DataHistory();
        dataHistory.setDeviceNo(deviceNo);
        dataHistory.setLampblack(new BigDecimal(0));
        dataHistory.setLampblackFlag("N");
        dataHistory.setPm(new BigDecimal(0));
        dataHistory.setPmFlag("N");
        dataHistory.setNmhc(new BigDecimal(0));
        dataHistory.setPmFlag("N");
        return dataHistory;
    }

    @Override
    public List<DataHistory> batchFindDataToDeviceNo(List<String> equipmentIDList) {
        List<DataHistory> dataHistoryList = new ArrayList<>(0);
        if (equipmentIDList.size() == 0)
            throw new ServiceException(ConstUtil.UNOK, "查询实时数据列表为空");
        for (String deviceNo : equipmentIDList) {
            DataHistory dataHistory = this.returnRealtimeData(deviceNo);
            dataHistoryList.add(dataHistory);
        }
        return dataHistoryList;
    }

    @Override
    public List<DataHistory> queryDataHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        LambdaQueryWrapper<DataHistory> lambdaQuery = Wrappers.<DataHistory>lambdaQuery();
        // 查询字段
        lambdaQuery.select(DataHistory::getDateTime, DataHistory::getLampblack, DataHistory::getLampblackFlag, DataHistory::getPm, DataHistory::getPmFlag, DataHistory::getNmhc, DataHistory::getNmhcFlag, DataHistory::getFanFlag, DataHistory::getPurifierFlag);
        lambdaQuery.eq(DataHistory::getDeviceNo, deviceNo);
        lambdaQuery.between(DataHistory::getDateTime, start, end);
        lambdaQuery.orderByAsc(DataHistory::getDateTime);
        List<DataHistory> dataHistories = new ArrayList<>(0);
        try {
            dataHistories = super.list(lambdaQuery);
        } catch (Exception e) {
            return dataHistories;
        }
        return dataHistories;
    }

    @Override
    public List<DataHistory> queryDataToDay(String deviceNo) {
        // 当前时间
        LocalDateTime nowToday = LocalDateTime.now();
        // 开始时间
        LocalDateTime start = LocalDateTime.of(nowToday.getYear(), nowToday.getMonthValue(), nowToday.getDayOfMonth(), 0, 0, 0, 0);
        LambdaQueryWrapper<DataHistory> lambdaQuery = Wrappers.<DataHistory>lambdaQuery();
        lambdaQuery.select(DataHistory::getLampblack, DataHistory::getPm, DataHistory::getNmhc, DataHistory::getDateTime);
        lambdaQuery.eq(DataHistory::getDeviceNo, deviceNo);
        lambdaQuery.between(DataHistory::getDateTime, start, nowToday);
        lambdaQuery.orderByAsc(DataHistory::getDateTime);
        List<DataHistory> dataHistories = new ArrayList<>(0);
        try {
            dataHistories = super.list(lambdaQuery);
        } catch (Exception e) {
            return dataHistories;
        }
        return dataHistories;
    }

}
