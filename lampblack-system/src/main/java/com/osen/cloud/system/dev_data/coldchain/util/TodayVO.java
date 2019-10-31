package com.osen.cloud.system.dev_data.coldchain.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 14:04
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayVO {

    private LocalDateTime dateTime;

    // 监控点01
    private BigDecimal T01;

    private BigDecimal H01;

    // 监控点02
    private BigDecimal T02;

    private BigDecimal H02;

    // 监控点03
    private BigDecimal T03;

    private BigDecimal H03;

    // 监控点04
    private BigDecimal T04;

    private BigDecimal H04;

}
