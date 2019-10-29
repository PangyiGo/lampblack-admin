package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocHistoryService extends IService<VocHistory> {

    /**
     * 插入VOC实时历史数据
     *
     * @param vocHistory 参数
     */
    void insertHistory(VocHistory vocHistory);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
