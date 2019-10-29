package com.osen.cloud.model.coldchain;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:07
 * Description: 冷链设备实时数据通用层接口
 */
@Repository
public interface ColdChainHourMapper extends BaseMapper<ColdChainHour> {

    void createNewTable(@Param("tableName") String tableName);
}
