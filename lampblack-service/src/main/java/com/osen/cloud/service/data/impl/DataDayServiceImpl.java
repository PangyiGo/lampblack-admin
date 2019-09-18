package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.DataDay;
import com.osen.cloud.model.data.DataDayMapper;
import com.osen.cloud.service.data.DataDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备天上传数据服务接口实现类
 */
@Service
public class DataDayServiceImpl extends ServiceImpl<DataDayMapper, DataDay> implements DataDayService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDayData(DataDay dataDay) {
        super.save(dataDay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }
}
