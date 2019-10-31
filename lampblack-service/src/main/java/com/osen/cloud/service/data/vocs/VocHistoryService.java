package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;

import java.util.List;

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

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    VocHistory getRealtime(String deviceNo);

    /**
     * 通过参数获取指定设备号的当天历史数据
     *
     * @param args     设备参数
     * @param deviceNo 设备号
     * @return 信息
     */
    List<VocHistory> getDataToday(String args, String deviceNo);
}
