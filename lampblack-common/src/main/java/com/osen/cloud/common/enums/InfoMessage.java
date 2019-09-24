package com.osen.cloud.common.enums;

import com.osen.cloud.common.utils.ConstUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 17:46
 * Description: 统一信息体
 */
@NoArgsConstructor
@AllArgsConstructor
public enum InfoMessage {

    /**
     * 异常信息体
     */
    UnknownSystem_Error(ConstUtil.UNOK, "系统未知异常"),

    NoFound_Error(ConstUtil.UNOK, "页面不存在"),

    InsertUser_Error(ConstUtil.UNOK, "账号重复添加"),

    InsertDevice_Error(ConstUtil.UNOK, "设备ID重复添加"),

    /**
     * 成功信息体
     */
    Success_OK(ConstUtil.OK, "请求成功"),

    Failed_Error(ConstUtil.UNOK, "请求失败"),

    Refresh_Failed(ConstUtil.UNOK, "令牌刷新异常"),

    /**
     * 提示信息体
     */
    User_Need_Authorization(ConstUtil.UNOK, "用户未登录"),

    User_Login_Failed(ConstUtil.UNOK, "账号或密码错误"),

    User_Login_Success(ConstUtil.OK, "登录成功"),

    User_NO_Access(ConstUtil.UNOK, "用户无权访问"),

    User_Logout_Success(ConstUtil.OK, "用户已成功退出登录"),

    User_Logout_Failed(ConstUtil.UNOK, "退出登录异常"),

    User_Login_Guoqi(ConstUtil.UNOK, "登录状态过期");

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
