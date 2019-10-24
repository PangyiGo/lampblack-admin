package com.osen.cloud.common.entity.dev_coldchain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 11:28
 * Description: 冷链实时数据模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coldchain_minute")
public class ColdChainMinute extends Model<ColdChainMinute> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    private LocalDateTime dateTime;

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
