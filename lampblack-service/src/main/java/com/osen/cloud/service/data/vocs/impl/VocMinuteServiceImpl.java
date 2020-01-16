package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocMinute;
import com.osen.cloud.model.vos.VocMinuteMapper;
import com.osen.cloud.service.data.vocs.VocMinuteService;
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
public class VocMinuteServiceImpl extends ServiceImpl<VocMinuteMapper, VocMinute> implements VocMinuteService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertMinute(VocMinute vocMinute) {
        super.save(vocMinute);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNewTable(String tableName) {
        baseMapper.createNewTable(tableName);
    }

    @Override
    public List<VocMinute> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo) {
        List<VocMinute> vocMinutes = new ArrayList<>(0);
        // 查询条件
        LambdaQueryWrapper<VocMinute> query = Wrappers.<VocMinute>lambdaQuery();
        query.eq(VocMinute::getDeviceNo, deviceNo);
        query.between(VocMinute::getDateTime, start, end).orderByAsc(VocMinute::getDateTime);
        try {
            vocMinutes = super.list(query);
        } catch (Exception e) {
            return vocMinutes;
        }
        return vocMinutes;
    }

    @Override
    public VocMinute getOneData(String deviceNo, LocalDateTime dateTime) {
        LambdaQueryWrapper<VocMinute> wrapper = Wrappers.<VocMinute>lambdaQuery().select(VocMinute::getDeviceNo).eq(VocMinute::getDeviceNo, deviceNo).eq(VocMinute::getDateTime, dateTime);
        try {
            return super.getOne(wrapper, true);
        } catch (Exception e) {
            return null;
        }
    }
}
