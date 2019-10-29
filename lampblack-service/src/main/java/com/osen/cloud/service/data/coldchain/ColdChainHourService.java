package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainHourService extends IService<ColdChainHour> {

    /**
     * 插入冷链小时历史数据
     *
     * @param coldChainHour 参数
     */
    void insertHour(ColdChainHour coldChainHour);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
