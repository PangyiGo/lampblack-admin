package com.osen.cloud.system.system_socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-10-25
 * Time: 8:57
 * Description: VOC数据转换模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocDataModel {

    private BigDecimal voc;

    private BigDecimal vocMax;

    private BigDecimal vocMin;

    private String vocFlag;

    private BigDecimal flow;

    private BigDecimal flowMax;

    private BigDecimal flowMin;

    private String flowFlag;

    private BigDecimal speed;

    private BigDecimal speedMax;

    private BigDecimal speedMin;

    private String speedFlag;

    private BigDecimal pressure;

    private BigDecimal pressureMax;

    private BigDecimal pressureMin;

    private String pressureFlag;

    private BigDecimal temp;

    private BigDecimal tempMax;

    private BigDecimal tempMin;

    private String tempFlag;
}
