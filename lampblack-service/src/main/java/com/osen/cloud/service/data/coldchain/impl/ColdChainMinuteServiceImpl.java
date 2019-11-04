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
}
