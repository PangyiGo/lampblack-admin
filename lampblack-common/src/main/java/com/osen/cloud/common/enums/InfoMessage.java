package com.osen.cloud.common.enums;

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
    UnknownSystem_Error(4001, "系统未知异常"),

    /**
     * 成功信息体
     */
    Success_OK(2001, "成功请求");

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
