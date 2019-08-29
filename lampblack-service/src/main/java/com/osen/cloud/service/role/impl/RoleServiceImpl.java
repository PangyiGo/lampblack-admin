package com.osen.cloud.service.role.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.UserRole;
import com.osen.cloud.model.role.RoleMapper;
import com.osen.cloud.service.role.RoleService;
import com.osen.cloud.service.user_role.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 15:01
 * Description: 基本角色服务接口实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Role> findByRoleId(List<Integer> rids) {
        return CollectionUtil.list(false, super.listByIds(rids));
    }

    @Override
    public List<Role> findRoleByUserId(Integer uid) {
        List<UserRole> byUserIdToRole = userRoleService.findByUserIdToRole(uid);
        if (CollectionUtil.isEmpty(byUserIdToRole)) {
            // 为空返回
            return null;
        }
        // 对应角色ID
        List<Integer> roleIds = new ArrayList<>();
        for (UserRole userRole : byUserIdToRole) {
            roleIds.add(userRole.getRoleId());
        }
        return findByRoleId(roleIds);
    }
}
