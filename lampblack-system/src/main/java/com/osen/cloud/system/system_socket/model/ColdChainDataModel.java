package com.osen.cloud.system.system_socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-10-25
 * Time: 8:59
 * Description: 冷链数据转换模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColdChainDataModel {

    // 监控点01
    private BigDecimal T01;

    private BigDecimal T01Max;

    private BigDecimal T01Min;

    private String T01Flag;

    private BigDecimal H01;

    private BigDecimal H01Max;

    private BigDecimal H01Min;

    private String H01Flag;

    // 监控点02
    private BigDecimal T02;

    private BigDecimal T02Max;

    private BigDecimal T02Min;

    private String T02Flag;

    private BigDecimal H02;

    private BigDecimal H02Max;

    private BigDecimal H02Min;

    private String H02Flag;

    // 监控点03
    private BigDecimal T03;

    private BigDecimal T03Max;

    private BigDecimal T03Min;

    private String T03Flag;

    private BigDecimal H03;

    private BigDecimal H03Max;

    private BigDecimal H03Min;

    private String H03Flag;

    // 监控点04
    private BigDecimal T04;

    private BigDecimal T04Max;

    private BigDecimal T04Min;

    private String T04Flag;

    private BigDecimal H04;

    private BigDecimal H04Max;

    private BigDecimal H04Min;

    private String H04Flag;

    // 经度
    private String longitude;

    // 纬度
    private String latitude;

}
