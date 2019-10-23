package com.osen.cloud.common.entity;

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
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 15:16
 * Description: 设备实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device")
public class Device extends Model<Device> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;

    private String name;

    private String province;

    private String city;

    private String area;

    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 监控地址
     */
    private String link;

    /**
     * 设备类型
     */
    private String type;

    private Integer isLive;

    private String channel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
