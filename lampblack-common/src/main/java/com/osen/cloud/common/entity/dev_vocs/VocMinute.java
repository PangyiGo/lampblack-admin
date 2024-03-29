package com.osen.cloud.common.entity.dev_vocs;

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
 * Time: 10:58
 * Description: VOC实时数据模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("voc_minute")
public class VocMinute extends Model<VocMinute> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    private LocalDateTime dateTime;

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
