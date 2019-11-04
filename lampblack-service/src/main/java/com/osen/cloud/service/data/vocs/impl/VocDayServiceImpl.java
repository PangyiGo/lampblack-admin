package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocDay;
import com.osen.cloud.model.vos.VocDayMapper;
import com.osen.cloud.service.data.vocs.VocDayService;
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
public class VocDayServiceImpl extends ServiceImpl<VocDayMapper, VocDay> implements VocDayService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDay(VocDay vocDay) {
        super.save(vocDay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<VocDay> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<VocDay> vocDays = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<VocDay> query = Wrappers.<VocDay>lambdaQuery();
        query.eq(VocDay::getDeviceNo, deviceNo);
        query.between(VocDay::getDateTime, start, end).orderByAsc(VocDay::getDateTime);
        try {
            vocDays = super.list(query);
        } catch (Exception e) {
            return vocDays;
        }
        return vocDays;
    }
}
