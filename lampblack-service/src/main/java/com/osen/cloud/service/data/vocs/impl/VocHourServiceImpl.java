package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocHour;
import com.osen.cloud.model.vos.VocHourMapper;
import com.osen.cloud.service.data.vocs.VocHourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:45
 * Description: VOC实时数据服务接口实现类
 */
@Service
public class VocHourServiceImpl extends ServiceImpl<VocHourMapper, VocHour> implements VocHourService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertHour(VocHour vocHour) {
        super.save(vocHour);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<VocHour> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<VocHour> vocHours = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<VocHour> query = Wrappers.<VocHour>lambdaQuery();
        query.eq(VocHour::getDeviceNo, deviceNo);
        query.between(VocHour::getDateTime, start, end).orderByAsc(VocHour::getDateTime);
        try {
            vocHours = super.list(query);
        } catch (Exception e) {
            return vocHours;
        }
        return vocHours;
    }
}
