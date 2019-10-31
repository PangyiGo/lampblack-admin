package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;

import java.util.List;

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

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    ColdChainHistory getRealtime(String deviceNo);

    /**
     * 获取指定设备号的当天最新历史数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    List<ColdChainHistory> getDataToday(String deviceNo);
}
