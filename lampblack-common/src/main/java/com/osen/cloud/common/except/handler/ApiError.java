package com.osen.cloud.common.except.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 17:14
 * Description: 异常统一格式
 */
@Data
public class ApiError {

    /**
     * 状态值
     */
    private Integer status;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 返回信息
     */
    private String message;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(Integer status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
