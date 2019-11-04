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
}
