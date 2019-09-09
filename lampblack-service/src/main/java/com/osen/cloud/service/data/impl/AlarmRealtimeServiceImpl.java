package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.AlarmRealtime;
import com.osen.cloud.model.data.AlarmRealtimeMapper;
import com.osen.cloud.service.data.AlarmRealtimeService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备实时报警记录服务接口实现类
 */
@Service
public class AlarmRealtimeServiceImpl extends ServiceImpl<AlarmRealtimeMapper, AlarmRealtime> implements AlarmRealtimeService {
}
