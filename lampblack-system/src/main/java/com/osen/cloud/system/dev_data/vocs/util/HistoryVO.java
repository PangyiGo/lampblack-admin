package com.osen.cloud.system.dev_data.vocs.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:29
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVO {

    private String deviceNo;

    private LocalDateTime dateTime;

    private BigDecimal voc;

    private BigDecimal flow;

    private BigDecimal speed;

    private BigDecimal pressure;

    private BigDecimal temp;

}
