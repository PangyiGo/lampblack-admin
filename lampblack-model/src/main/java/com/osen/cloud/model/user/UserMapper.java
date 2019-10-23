package com.osen.cloud.model.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.osen.cloud.common.entity.system_user.User;
import org.springframework.stereotype.Repository;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:46
 * Description: 用户基本数据模型
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
