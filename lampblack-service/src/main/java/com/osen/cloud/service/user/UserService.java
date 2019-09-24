package com.osen.cloud.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.User;

import java.util.List;
import java.util.Map;

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

    /**
     * 分页查询所有用户列表
     *
     * @param number  当前页数
     * @param company 查询条件
     * @return 信息
     */
    Map<String, Object> findAllUserToPage(Integer number, String company);

    /**
     * 通过用户账号删除指定用户
     *
     * @param account 用户账号
     * @return 信息
     */
    boolean deleteUserByAccount(User account);
}
