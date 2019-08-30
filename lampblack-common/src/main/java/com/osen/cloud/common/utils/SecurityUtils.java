package com.osen.cloud.common.utils;

import cn.hutool.json.JSONObject;
import com.osen.cloud.common.except.type.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 18:43
 * Description: 获取系统用户信息
 */
public class SecurityUtils {

    private static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("username", String.class);
    }

    /**
     * 获取系统用户id
     *
     * @return 系统用户id
     */
    public static Integer getUserId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Integer.class);
    }
}
