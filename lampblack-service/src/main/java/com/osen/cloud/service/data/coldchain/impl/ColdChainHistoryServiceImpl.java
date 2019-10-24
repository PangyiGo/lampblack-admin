package com.osen.cloud.service.data.coldchain.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;
import com.osen.cloud.model.coldchain.ColdChainHistoryMapper;
import com.osen.cloud.service.data.coldchain.ColdChainHistoryService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 15:14
 * Description: 冷链实时数据通用服务接口实现类
 */
@Service
public class ColdChainHistoryServiceImpl extends ServiceImpl<ColdChainHistoryMapper, ColdChainHistory> implements ColdChainHistoryService {
}
