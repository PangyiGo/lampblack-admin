package com.osen.cloud.model.vos;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.dev_vocs.VocMinute;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:00
 * Description: VOC实时数据通用层接口
 */
@Repository
public interface VocMinuteMapper extends BaseMapper<VocMinute> {

    void createNewTable(@Param("tableName") String tableName);
}
