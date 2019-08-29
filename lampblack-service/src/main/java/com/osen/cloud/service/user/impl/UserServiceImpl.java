package com.osen.cloud.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.model.user.UserMapper;
import com.osen.cloud.service.user.UserService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:50
 * Description: 基本用户服务接口实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
