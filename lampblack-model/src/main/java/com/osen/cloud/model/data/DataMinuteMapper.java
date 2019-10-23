package com.osen.cloud.model.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.lampblack.DataMinute;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:17
 * Description: 设备分钟上传数据模型
 */
public interface DataMinuteMapper extends BaseMapper<DataMinute> {

    void createNewTable(@Param("tableName") String tableName);
}