package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocAlarm;
import com.osen.cloud.model.vos.VocAlarmMapper;
import com.osen.cloud.service.data.vocs.VocAlarmService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:45
 * Description: VOC实时数据服务接口实现类
 */
@Service
public class VocAlarmServiceImpl extends ServiceImpl<VocAlarmMapper, VocAlarm> implements VocAlarmService {
}