package com.osen.cloud.system.dev_data.vocs.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 15:59
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayVO {

    private LocalDateTime dateTime;

    private BigDecimal data;

}
