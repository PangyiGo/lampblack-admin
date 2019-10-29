package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.model.vos.VocHistoryMapper;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:45
 * Description: VOC实时数据服务接口实现类
 */
@Service
public class VocHistoryServiceImpl extends ServiceImpl<VocHistoryMapper, VocHistory> implements VocHistoryService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHistory(VocHistory vocHistory) {
        super.save(vocHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }
}
