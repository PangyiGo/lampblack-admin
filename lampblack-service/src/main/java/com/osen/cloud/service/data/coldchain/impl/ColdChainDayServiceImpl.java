package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainDay;
import com.osen.cloud.model.coldchain.ColdChainDayMapper;
import com.osen.cloud.service.data.coldchain.ColdChainDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 15:14
 * Description: 冷链实时数据通用服务接口实现类
 */
@Service
public class ColdChainDayServiceImpl extends ServiceImpl<ColdChainDayMapper, ColdChainDay> implements ColdChainDayService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDay(ColdChainDay coldChainDay) {
        super.save(coldChainDay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }
}
