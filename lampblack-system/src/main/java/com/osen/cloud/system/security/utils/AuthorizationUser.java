package com.osen.cloud.system.security.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 17:38
 * Description: 认证参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationUser {

    private String username;

    private String password;
}
