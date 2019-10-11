package com.osen.cloud.system.data.util;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-11
 * Time: 14:07
 * Description: 返回数据模型
 */
@Data
public class DataHistoryRealVO implements Serializable {

    private LocalDateTime dateTime;

    private BigDecimal lampblack;

    private BigDecimal pm;

    private BigDecimal nmhc;

}
