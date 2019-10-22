package com.osen.cloud.system.socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 转换中间件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampblackDataModel implements Serializable {

    private BigDecimal lampblack;

    private BigDecimal lampblackMin;

    private BigDecimal lampblackMax;

    private String lampblackFlag;

    private BigDecimal pm;

    private BigDecimal pmMin;

    private BigDecimal pmMax;

    private String pmFlag;

    private BigDecimal nmhc;

    private BigDecimal nmhcMin;

    private BigDecimal nmhcMax;

    private String nmhcFlag;

    private Integer fanFlag;

    private Integer purifierFlag;
}
