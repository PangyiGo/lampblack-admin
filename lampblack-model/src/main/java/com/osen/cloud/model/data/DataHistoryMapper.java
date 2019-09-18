package com.osen.cloud.model.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.DataHistory;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:17
 * Description: 设备上传实时数据模型
 */
public interface DataHistoryMapper extends BaseMapper<DataHistory> {

    void createNewTable(@Param("tableName") String tableName);
}
