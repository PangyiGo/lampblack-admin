package com.osen.cloud.system.dev_data.coldchain.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-11-05
 * Time: 14:38
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataVO {

    private LocalDateTime dateTime;

    private BigDecimal temp;

    private BigDecimal damp;
}
