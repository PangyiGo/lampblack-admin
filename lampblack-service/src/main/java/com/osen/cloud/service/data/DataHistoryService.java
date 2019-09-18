package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.DataHistory;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:24
 * Description: 设备实时上传数据服务接口
 */
public interface DataHistoryService extends IService<DataHistory> {

    /**
     * 插入设备实时数据
     *
     * @param dataHistory 数据
     */
    void insertRealtimeData(DataHistory dataHistory);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
