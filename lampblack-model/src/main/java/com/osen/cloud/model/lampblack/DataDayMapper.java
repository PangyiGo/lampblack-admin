package com.osen.cloud.model.lampblack;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.dev_lampblack.DataDay;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:17
 * Description: 设备天上传数据模型
 */
public interface DataDayMapper extends BaseMapper<DataDay> {

    void createNewTable(@Param("tableName") String tableName);

}
