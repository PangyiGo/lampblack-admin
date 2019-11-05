package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.coldchain.ColdChainMonitorMapper;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public boolean updateMonitor(ColdChainMonitor coldChainMonitor) {
        coldChainMonitor.setUpdateTime(LocalDateTime.now());
        LambdaUpdateWrapper<ColdChainMonitor> update = Wrappers.<ColdChainMonitor>lambdaUpdate();
        update.eq(ColdChainMonitor::getDeviceNo, coldChainMonitor.getDeviceNo());
        return super.update(coldChainMonitor, update);
    }

    @Override
    public Map<String, Object> getAllMonitorToUser(List<String> deviceNos, Map<String, Object> params) {
        LambdaQueryWrapper<ColdChainMonitor> query = Wrappers.<ColdChainMonitor>lambdaQuery();
        String conditional = (String) params.get("search");
        int pageNumber = (int) params.get("pageNumber");
        Page<ColdChainMonitor> coldChainMonitorPage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
        // 查询条件
        query.in(ColdChainMonitor::getDeviceNo, deviceNos);
        if (StringUtils.isNotEmpty(conditional))
            query.like(ColdChainMonitor::getM01, conditional).or().like(ColdChainMonitor::getM02, conditional).or().like(ColdChainMonitor::getM03, conditional).or().like(ColdChainMonitor::getM04, conditional).or().like(ColdChainMonitor::getDeviceNo, conditional);
        // 分页
        IPage<ColdChainMonitor> coldChainMonitorIPage = super.page(coldChainMonitorPage, query);
        // 结果
        Map<String, Object> resultMap = new HashMap<>(0);
        if (coldChainMonitorIPage.getTotal() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("monitors", coldChainMonitorIPage);
        } else {
            resultMap.put("total", coldChainMonitorIPage.getTotal());
            resultMap.put("monitors", coldChainMonitorIPage.getRecords());
        }
        return resultMap;
    }
}
