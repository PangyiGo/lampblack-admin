package com.osen.cloud.system.user.vo;

import com.osen.cloud.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-08-30
 * Time: 10:40
 * Description: 添加用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertUserVo implements Serializable {

    @NotNull
    public User user;

    @NotNull
    public List<Integer> roles;
}
