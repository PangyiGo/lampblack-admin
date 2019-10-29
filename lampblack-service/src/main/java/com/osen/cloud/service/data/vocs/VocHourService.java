package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocHour;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocHourService extends IService<VocHour> {

    /**
     * 插入VOC小时历史数据
     *
     * @param vocHour 参数
     */
    void insertHour(VocHour vocHour);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
