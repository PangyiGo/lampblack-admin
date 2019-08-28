package com.osen.cloud.common.except.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.enums.InfoMessage;
import com.osen.cloud.common.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 17:16
 * Description: 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理未知异常
     *
     * @param exception 异常类型
     * @return 返回信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        // 打印异常信息
        log.error(ThrowableUtil.getStackTrace(exception));
        ApiError apiError = new ApiError(InfoMessage.UnknownSystem_Error.getCode(), InfoMessage.UnknownSystem_Error.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * 统一返回
     *
     * @param apiError 异常信息
     * @return 返回前端异常
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        log.error("Return exception info：" + JSON.toJSONString(apiError));
        return new ResponseEntity<ApiError>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
