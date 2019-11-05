package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;
import com.osen.cloud.model.coldchain.ColdChainHourMapper;
import com.osen.cloud.service.data.coldchain.ColdChainHourService;
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
public class ColdChainHourServiceImpl extends ServiceImpl<ColdChainHourMapper, ColdChainHour> implements ColdChainHourService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHour(ColdChainHour coldChainHour) {
        super.save(coldChainHour);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<ColdChainHour> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<ColdChainHour> coldChainHours = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<ColdChainHour> query = Wrappers.<ColdChainHour>lambdaQuery();
        query.eq(ColdChainHour::getDeviceNo, deviceNo);
        query.between(ColdChainHour::getDateTime, start, end).orderByAsc(ColdChainHour::getDateTime);
        try {
            coldChainHours = super.list(query);
        } catch (Exception e) {
            return coldChainHours;
        }
        return coldChainHours;
    }

    @Override
    public List<ColdChainHour> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo, String monitor) {
        List<ColdChainHour> chainHours = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<ColdChainHour> query = Wrappers.<ColdChainHour>lambdaQuery();
        switch (monitor) {
            case "m01":
                query.select(ColdChainHour::getT01, ColdChainHour::getH01, ColdChainHour::getDateTime);
                break;
            case "m02":
                query.select(ColdChainHour::getT02, ColdChainHour::getH02, ColdChainHour::getDateTime);
                break;
            case "m03":
                query.select(ColdChainHour::getT03, ColdChainHour::getH03, ColdChainHour::getDateTime);
                break;
            case "m04":
                query.select(ColdChainHour::getT04, ColdChainHour::getH04, ColdChainHour::getDateTime);
                break;
        }
        query.eq(ColdChainHour::getDeviceNo, deviceNo);
        query.between(ColdChainHour::getDateTime, start, end).orderByAsc(ColdChainHour::getDateTime);
        try {
            chainHours = super.list(query);
        } catch (Exception e) {
            return chainHours;
        }
        return chainHours;
    }
}
