package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.AlarmHistory;
import com.osen.cloud.model.data.AlarmHistoryMapper;
import com.osen.cloud.service.data.AlarmHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
