package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_lampblack.AlarmHistory;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.data.AlarmHistoryMapper;
import com.osen.cloud.service.data.AlarmHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备报警历史记录服务接口实现类
 */
@Service
public class AlarmHistoryServiceImpl extends ServiceImpl<AlarmHistoryMapper, AlarmHistory> implements AlarmHistoryService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAlarmData(AlarmHistory alarmHistory) {
        super.save(alarmHistory);
    }

    @Override
    public Map<String, Object> queryAlarmHistoryByAccount(Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String start = (String) params.get("startTime");
        String end = (String) params.get("endTime");
        int pageNumber = (int) params.get("pageNumber");
        // 日期时间格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        // 分页
        Page<AlarmHistory> alarmHistoryPage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
        // 条件封装
        LambdaQueryWrapper<AlarmHistory> queryWrapper = Wrappers.<AlarmHistory>lambdaQuery();
        queryWrapper.eq(AlarmHistory::getDeviceNo, deviceNo);
        queryWrapper.between(AlarmHistory::getDateTime, startTime, endTime);
        queryWrapper.orderByAsc(AlarmHistory::getDateTime);
        // 查询
        IPage<AlarmHistory> alarmHistoryIPage = super.page(alarmHistoryPage, queryWrapper);
        // 结果封装
        Map<String, Object> resultMap = new HashMap<>(0);
        if (alarmHistoryIPage.getTotal() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("alarmHistory", null);
        } else {
            resultMap.put("total", alarmHistoryIPage.getTotal());
            resultMap.put("alarmHistory", alarmHistoryIPage.getRecords());
        }
        return resultMap;
    }
}
