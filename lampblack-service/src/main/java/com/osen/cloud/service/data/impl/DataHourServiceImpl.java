package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.DataHour;
import com.osen.cloud.model.data.DataHourMapper;
import com.osen.cloud.service.data.DataHourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备小时上传数据服务接口实现类
 */
@Service
public class DataHourServiceImpl extends ServiceImpl<DataHourMapper, DataHour> implements DataHourService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHourData(DataHour dataHour) {
        super.save(dataHour);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }
}
