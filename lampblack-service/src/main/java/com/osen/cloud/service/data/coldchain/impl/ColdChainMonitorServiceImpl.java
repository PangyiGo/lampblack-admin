package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.model.coldchain.ColdChainMonitorMapper;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 15:14
 * Description: 冷链实时数据通用服务接口实现类
 */
@Service
public class ColdChainMonitorServiceImpl extends ServiceImpl<ColdChainMonitorMapper, ColdChainMonitor> implements ColdChainMonitorService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertColdMonitor(ColdChainMonitor coldChainMonitor) {
        super.save(coldChainMonitor);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDefaultMonitor(String deviceNo) {
        ColdChainMonitor coldChainMonitor = new ColdChainMonitor();
        coldChainMonitor.setDeviceNo(deviceNo);
        coldChainMonitor.setM01("监控点#1");
        coldChainMonitor.setM02("监控点#2");
        coldChainMonitor.setM03("监控点#3");
        coldChainMonitor.setM04("监控点#4");
        coldChainMonitor.setCreateTime(LocalDateTime.now());
        coldChainMonitor.setUpdateTime(LocalDateTime.now());
        super.save(coldChainMonitor);
    }

    @Override
    public ColdChainMonitor getMonitorToDeviceNo(String deviceNo) {
        LambdaQueryWrapper<ColdChainMonitor> wrapper = Wrappers.<ColdChainMonitor>lambdaQuery().eq(ColdChainMonitor::getDeviceNo, deviceNo);
        try {
            return super.getOne(wrapper, true);
        } catch (Exception e) {
            return null;
        }
    }
}
