package com.osen.cloud.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.User;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:49
 * Description: 基本用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 信息
     */
    User findByUsername(String username);

    /**
     * 添加新用户
     *
     * @param user  用户
     * @param roles 对应角色ID
     * @return 信息
     */
    boolean create(User user, List<Integer> roles);
}
