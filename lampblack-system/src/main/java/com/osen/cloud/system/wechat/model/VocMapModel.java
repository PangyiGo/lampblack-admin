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
public class VocMapModel {

    private String name;

    private String deviceNo;

    private Double longitude;

    private Double latitude;

    private String type;

    private Integer isLive;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private BigDecimal voc;

    private String vocFlag;

    private BigDecimal flow;

    private String flowFlag;

    private BigDecimal speed;

    private String speedFlag;

    private BigDecimal pressure;

    private String pressureFlag;

    private BigDecimal temp;

    private String tempFlag;
}
