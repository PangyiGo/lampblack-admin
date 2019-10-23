package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_lampblack.DataDay;
import com.osen.cloud.model.data.DataDayMapper;
import com.osen.cloud.service.data.DataDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备天上传数据服务接口实现类
 */
@Service
public class DataDayServiceImpl extends ServiceImpl<DataDayMapper, DataDay> implements DataDayService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDayData(DataDay dataDay) {
        super.save(dataDay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<DataDay> queryDataDayByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        LambdaQueryWrapper<DataDay> lambdaQuery = Wrappers.<DataDay>lambdaQuery();
        // 查询字段
        lambdaQuery.select(DataDay::getDateTime, DataDay::getLampblack, DataDay::getLampblackFlag, DataDay::getPm, DataDay::getPmFlag, DataDay::getNmhc, DataDay::getNmhcFlag, DataDay::getFanFlag, DataDay::getPurifierFlag);
        lambdaQuery.eq(DataDay::getDeviceNo, deviceNo);
        lambdaQuery.between(DataDay::getDateTime, start, end);
        lambdaQuery.orderByAsc(DataDay::getDateTime);
        List<DataDay> dataDays = new ArrayList<>(0);
        try {
            dataDays = super.list(lambdaQuery);
        } catch (Exception e) {
            return dataDays;
        }
        return dataDays;
    }
}
