package com.osen.cloud.service.authorization.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.model.authorization.AuthorizationMapper;
import com.osen.cloud.service.authorization.AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:36
 * Description: 授权认证服务实现类
 */
@Service
@Transactional
public class AuthorizationServiceImpl extends ServiceImpl<AuthorizationMapper, User> implements AuthorizationService {

    @Override
    public User findByUsername(String username) {
        return null;
    }
}
