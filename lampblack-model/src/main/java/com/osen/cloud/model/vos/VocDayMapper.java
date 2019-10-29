package com.osen.cloud.model.vos;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.dev_vocs.VocDay;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:00
 * Description: VOC实时数据通用层接口
 */
@Repository
public interface VocDayMapper extends BaseMapper<VocDay> {

    void createNewTable(@Param("tableName") String tableName);
}
