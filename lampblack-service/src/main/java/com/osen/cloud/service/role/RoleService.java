package com.osen.cloud.service.role;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.system_user.Role;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 15:01
 * Description: 基本角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色ID批量查询角色
     *
     * @param rids 多个角色ID
     * @return 数据
     */
    List<Role> findByRoleId(List<Integer> rids);

    /**
     * 根据用户ID查询对应角色
     *
     * @param uid 用户ID
     * @return 数据
     */
    List<Role> findRoleByUserId(Integer uid);
}
