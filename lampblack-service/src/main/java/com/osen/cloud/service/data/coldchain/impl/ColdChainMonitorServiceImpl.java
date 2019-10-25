package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.model.coldchain.ColdChainMonitorMapper;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
