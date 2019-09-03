package com.osen.cloud.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.common.entity.UserRole;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.user.UserMapper;
import com.osen.cloud.service.role.RoleService;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.service.user_role.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.osen.cloud.common.enums.InfoMessage.InsertUser_Error;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:50
 * Description: 基本用户服务接口实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().eq(User::getAccount, username);
        return super.getOne(wrapper, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(User user, List<Integer> roles) {
        if (this.findByUsername(user.getAccount()) != null)
            throw new ServiceException(InsertUser_Error.getCode(), InsertUser_Error.getMessage());
        //插入角色是否异常
        Collection<Role> listByIds = roleService.listByIds(roles);
        if (listByIds.size() != roles.size())
            return false;
        //初始密码
        user.setPassword(ConstUtil.INIT_PASSWORD);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        //添加用户
        if (super.save(user)) {
            Integer userId = user.getId();
            List<UserRole> userRoles = new ArrayList<>();
            for (Integer role : roles) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(role);
                userRoles.add(userRole);
            }
            return userRoleService.saveBatch(userRoles);
        }
        return false;
    }
}
