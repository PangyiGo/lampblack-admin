package com.osen.cloud.common.entity.dev_lampblack;

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
 * Date: 2019-08-28
 * Time: 15:22
 * Description: 设备数据每分钟记录实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("data_history")
public class DataHistory extends Model<DataHistory> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    private LocalDateTime dateTime;

    private BigDecimal lampblack;

    private String lampblackFlag;

    private BigDecimal pm;

    private String pmFlag;

    private BigDecimal nmhc;

    private String nmhcFlag;

    private Integer fanFlag;

    private Integer purifierFlag;
}

