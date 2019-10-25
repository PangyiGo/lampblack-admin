package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainHistoryService extends IService<ColdChainHistory> {

    /**
     * 插入冷链实时历史数据
     *
     * @param coldChainHistory 参数
     */
    void insertHistory(ColdChainHistory coldChainHistory);
}
