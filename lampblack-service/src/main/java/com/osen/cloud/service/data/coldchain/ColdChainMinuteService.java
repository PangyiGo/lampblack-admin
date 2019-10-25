package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMinute;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainMinuteService extends IService<ColdChainMinute> {

    /**
     * 插入冷链分钟历史数据
     *
     * @param coldChainMinute 参数
     */
    void insertMinute(ColdChainMinute coldChainMinute);
}
