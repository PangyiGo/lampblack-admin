package com.osen.cloud.system.wechat.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2020-01-15
 * Time: 11:20
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColdChainMapModel {

    private String name;

    private String deviceNo;

    private Double longitude;

    private Double latitude;

    private String type;

    private Integer isLive;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    // 监控点01
    private BigDecimal T01;

    private String T01Flag;

    private BigDecimal H01;

    private String H01Flag;

    private String M01;

    // 监控点02
    private BigDecimal T02;

    private String T02Flag;

    private BigDecimal H02;

    private String H02Flag;

    private String M02;

    // 监控点03
    private BigDecimal T03;

    private String T03Flag;

    private BigDecimal H03;

    private String H03Flag;

    private String M03;

    // 监控点04
    private BigDecimal T04;

    private String T04Flag;

    private BigDecimal H04;

    private String H04Flag;

    private String M04;
}
