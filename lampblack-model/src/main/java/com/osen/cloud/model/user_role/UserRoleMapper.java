package com.osen.cloud.model.user_role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.system_user.UserRole;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:47
 * Description: 用户角色关联数据模型
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
