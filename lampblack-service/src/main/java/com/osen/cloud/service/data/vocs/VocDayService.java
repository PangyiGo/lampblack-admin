package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocDay;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocDayService extends IService<VocDay> {

    /**
     * 插入天数历史数据
     *
     * @param vocDay 参数
     */
    void insertDay(VocDay vocDay);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
