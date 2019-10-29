package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;
import com.osen.cloud.model.coldchain.ColdChainHourMapper;
import com.osen.cloud.service.data.coldchain.ColdChainHourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
