package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMinute;
import com.osen.cloud.model.coldchain.ColdChainMinuteMapper;
import com.osen.cloud.service.data.coldchain.ColdChainMinuteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 15:14
 * Description: 冷链实时数据通用服务接口实现类
 */
@Service
public class ColdChainMinuteServiceImpl extends ServiceImpl<ColdChainMinuteMapper, ColdChainMinute> implements ColdChainMinuteService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertMinute(ColdChainMinute coldChainMinute) {
        super.save(coldChainMinute);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<ColdChainMinute> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<ColdChainMinute> coldChainMinutes = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<ColdChainMinute> query = Wrappers.<ColdChainMinute>lambdaQuery();
        query.eq(ColdChainMinute::getDeviceNo, deviceNo);
        query.between(ColdChainMinute::getDateTime, start, end).orderByAsc(ColdChainMinute::getDateTime);
        try {
            coldChainMinutes = super.list(query);
        } catch (Exception e) {
            return coldChainMinutes;
        }
        return coldChainMinutes;
    }

    @Override
    public List<ColdChainMinute> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo, String monitor) {
        List<ColdChainMinute> chainMinutes = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<ColdChainMinute> query = Wrappers.<ColdChainMinute>lambdaQuery();
        switch (monitor) {
            case "m01":
                query.select(ColdChainMinute::getT01, ColdChainMinute::getH01, ColdChainMinute::getDateTime);
                break;
            case "m02":
                query.select(ColdChainMinute::getT02, ColdChainMinute::getH02, ColdChainMinute::getDateTime);
                break;
            case "m03":
                query.select(ColdChainMinute::getT03, ColdChainMinute::getH03, ColdChainMinute::getDateTime);
                break;
            case "m04":
                query.select(ColdChainMinute::getT04, ColdChainMinute::getH04, ColdChainMinute::getDateTime);
                break;
        }
        query.eq(ColdChainMinute::getDeviceNo, deviceNo);
        query.between(ColdChainMinute::getDateTime, start, end).orderByAsc(ColdChainMinute::getDateTime);
        try {
            chainMinutes = super.list(query);
        } catch (Exception e) {
            return chainMinutes;
        }
        return chainMinutes;
    }
}
