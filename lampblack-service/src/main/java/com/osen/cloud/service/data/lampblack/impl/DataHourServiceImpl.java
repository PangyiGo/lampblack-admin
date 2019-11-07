package com.osen.cloud.service.data.lampblack.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_lampblack.DataHour;
import com.osen.cloud.model.lampblack.DataHourMapper;
import com.osen.cloud.service.data.lampblack.DataHourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备小时上传数据服务接口实现类
 */
@Service
public class DataHourServiceImpl extends ServiceImpl<DataHourMapper, DataHour> implements DataHourService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHourData(DataHour dataHour) {
        super.save(dataHour);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<DataHour> queryDataHourByDate(LocalDateTime start, LocalDateTime end, String deviceNo,int type) {
        LambdaQueryWrapper<DataHour> lambdaQuery = Wrappers.<DataHour>lambdaQuery();
        // 查询字段
        switch (type){
            case 1:
                lambdaQuery.select(DataHour::getDateTime, DataHour::getLampblack, DataHour::getLampblackFlag, DataHour::getPm, DataHour::getPmFlag, DataHour::getNmhc, DataHour::getNmhcFlag, DataHour::getFanFlag, DataHour::getPurifierFlag);
                break;
            case 2:
                lambdaQuery.select(DataHour::getDateTime, DataHour::getLampblack, DataHour::getPm,  DataHour::getNmhc, DataHour::getFanFlag, DataHour::getPurifierFlag);
                break;
        }
        lambdaQuery.eq(DataHour::getDeviceNo, deviceNo);
        lambdaQuery.between(DataHour::getDateTime, start, end);
        lambdaQuery.orderByAsc(DataHour::getDateTime);
        List<DataHour> dataHours = new ArrayList<>(0);
        try {
            dataHours = super.list(lambdaQuery);
        } catch (Exception e) {
            return dataHours;
        }
        return dataHours;
    }
}
