package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocMinute;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocMinuteService extends IService<VocMinute> {

    /**
     * 插入VOC分钟历史数据
     *
     * @param vocMinute 参数
     */
    void insertMinute(VocMinute vocMinute);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);
}
