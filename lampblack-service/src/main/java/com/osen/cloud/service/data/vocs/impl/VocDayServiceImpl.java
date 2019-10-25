package com.osen.cloud.service.data.vocs.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_vocs.VocDay;
import com.osen.cloud.model.vos.VocDayMapper;
import com.osen.cloud.service.data.vocs.VocDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
