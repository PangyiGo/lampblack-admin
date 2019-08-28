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
 * Time: 14:27
 * Description: 角色实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("system_role")
public class Role extends Model<Role> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String remark;

    private LocalDateTime createTime;

}
