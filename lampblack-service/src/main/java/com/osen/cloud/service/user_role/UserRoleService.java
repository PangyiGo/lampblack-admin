package com.osen.cloud.service.user_role;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.system_user.UserRole;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:51
 * Description: 用户关联服务接口
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID查询关联角色
     *
     * @param uid 用户ID
     * @return 关联数据
     */
    List<UserRole> findByUserIdToRole(Integer uid);
}
