package com.osen.cloud.service.authorization.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.model.authorization.AuthorizationMapper;
import com.osen.cloud.service.authorization.AuthorizationService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:36
 * Description: 授权认证服务实现类
 */
@Service
public class AuthorizationServiceImpl extends ServiceImpl<AuthorizationMapper, User> implements AuthorizationService {

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            // 查询多条记录，抛出异常
            LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getAccount, User::getId, User::getPassword, User::getStatus).eq(User::getAccount, username);
            user = super.getOne(wrapper, true);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

}
