package com.osen.cloud.model.authorization;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.User;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:32
 * Description: 授权认证模型
 */
@Repository
public interface AuthorizationMapper extends BaseMapper<User> {
}
