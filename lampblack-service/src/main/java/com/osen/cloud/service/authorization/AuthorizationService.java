package com.osen.cloud.service.authorization;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.system_user.User;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:34
 * Description: 授权认证服务接口
 */
public interface AuthorizationService extends IService<User> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 指定用户
     */
    User findByUsername(String username);

}
