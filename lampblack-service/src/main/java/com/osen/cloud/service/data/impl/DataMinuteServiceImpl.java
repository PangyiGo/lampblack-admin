package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_lampblack.DataMinute;
import com.osen.cloud.model.lampblack.DataMinuteMapper;
import com.osen.cloud.service.data.DataMinuteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备分钟上传数据服务接口实现类
 */
@Service
public class DataMinuteServiceImpl extends ServiceImpl<DataMinuteMapper, DataMinute> implements DataMinuteService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertMinuteData(DataMinute dataMinute) {
        super.save(dataMinute);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<DataMinute> queryDataMinuteByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        LambdaQueryWrapper<DataMinute> lambdaQuery = Wrappers.<DataMinute>lambdaQuery();
        // 查询字段
        lambdaQuery.select(DataMinute::getDateTime, DataMinute::getLampblack, DataMinute::getLampblackFlag, DataMinute::getPm, DataMinute::getPmFlag, DataMinute::getNmhc, DataMinute::getNmhcFlag, DataMinute::getFanFlag, DataMinute::getPurifierFlag);
        lambdaQuery.eq(DataMinute::getDeviceNo, deviceNo);
        lambdaQuery.between(DataMinute::getDateTime, start, end);
        lambdaQuery.orderByAsc(DataMinute::getDateTime);
        List<DataMinute> dataMinutes = new ArrayList<>(0);
        try {
            dataMinutes = super.list(lambdaQuery);
        } catch (Exception e) {
            return dataMinutes;
        }
        return dataMinutes;
    }
}
