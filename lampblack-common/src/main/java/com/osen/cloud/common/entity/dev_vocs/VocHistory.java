package com.osen.cloud.common.entity.dev_vocs;

import com.alibaba.fastjson.annotation.JSONField;
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
@TableName("voc_history")
public class VocHistory extends Model<VocHistory> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

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
