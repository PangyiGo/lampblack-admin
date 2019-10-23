package com.osen.cloud.common.entity.system_user;

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
 * Time: 15:11
 * Description: 权限菜单实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("system_menu")
public class Menu extends Model<Menu> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String component;

    private Integer parentId;

    private Integer isFrame;

    private Integer sort;

    private String icon;

    private String path;

    private String remark;

    private LocalDateTime createTime;
}
