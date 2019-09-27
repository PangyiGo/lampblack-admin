package com.osen.cloud.service.data.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.DataHistory;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.data.DataHistoryMapper;
import com.osen.cloud.service.data.DataHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertRealtimeData(DataHistory dataHistory) {
        super.save(dataHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public DataHistory returnRealtimeData(String deviceNo) {
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(ConstUtil.DATA_KEY);
        Boolean hasKey = operations.hasKey(deviceNo);
        if (hasKey) {
            String dataJson = (String) operations.get(deviceNo);
            return JSON.parseObject(dataJson, DataHistory.class);
        }
        return new DataHistory();
    }
}
