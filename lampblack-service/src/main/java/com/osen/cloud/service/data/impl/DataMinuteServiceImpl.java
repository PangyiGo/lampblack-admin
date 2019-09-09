package com.osen.cloud.service.data.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.DataMinute;
import com.osen.cloud.model.data.DataMinuteMapper;
import com.osen.cloud.service.data.DataMinuteService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:34
 * Description: 设备分钟上传数据服务接口实现类
 */
@Service
public class DataMinuteServiceImpl extends ServiceImpl<DataMinuteMapper, DataMinute> implements DataMinuteService {
}
