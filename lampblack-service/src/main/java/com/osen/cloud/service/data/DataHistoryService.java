package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.DataHistory;

import java.util.List;

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

    /**
     * 根据设备号返回指定设备实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    DataHistory returnRealtimeData(String deviceNo);

    /**
     * 批量查询设备列表实时数据
     *
     * @param equipmentIDList 设备号列表
     * @return 信息
     */
    List<DataHistory> batchFindDataToDeviceNo(List<String> equipmentIDList);
}
