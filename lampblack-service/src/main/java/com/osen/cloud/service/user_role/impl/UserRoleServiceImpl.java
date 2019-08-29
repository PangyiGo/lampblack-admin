package com.osen.cloud.service.user_role.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.UserRole;
import com.osen.cloud.model.user_role.UserRoleMapper;
import com.osen.cloud.service.user_role.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:52
 * Description: 用户角色关联服务接口实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<UserRole> findByUserIdToRole(Integer uid) {
        // 查询条件
        LambdaQueryWrapper<UserRole> queryWrapper = Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, uid);
        return super.list(queryWrapper);
    }
}
