package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.DataHistory;
import com.osen.cloud.model.data.DataHistoryMapper;
import com.osen.cloud.service.data.DataHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备时候死数据上传服务接口实现类
 */
@Service
public class DataHistoryServiceImpl extends ServiceImpl<DataHistoryMapper, DataHistory> implements DataHistoryService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertRealtimeData(DataHistory dataHistory) {
        super.save(dataHistory);
    }
}
