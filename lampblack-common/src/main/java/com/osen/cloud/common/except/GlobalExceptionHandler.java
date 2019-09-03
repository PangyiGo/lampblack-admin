package com.osen.cloud.common.except;

import com.osen.cloud.common.except.type.RunRequestException;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.ThrowableUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.osen.cloud.common.enums.InfoMessage.*;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 18:41
 * Description: 统一异常处理程序
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 未知异常处理
     *
     * @param e 异常
     * @return 信息
     */
    @ExceptionHandler(value = Throwable.class)
    public RestResult exceptionHandle(Throwable e) {
        //打印错误日志
        log.error(ThrowableUtil.getStackTrace(e));
        return RestResultUtil.error(UnknownSystem_Error.getCode(), UnknownSystem_Error.getMessage());
    }

    /**
     * 404异常处理
     *
     * @return 信息
     */
    @ExceptionHandler(value = NotFoundException.class)
    public RestResult notfound(NotFoundException noFound) {
        // 打印错误日志
        log.error(ThrowableUtil.getStackTrace(noFound));
        return RestResultUtil.error(NoFound_Error.getCode(), NoFound_Error.getMessage());
    }

    /**
     * 处理自定义异常
     *
     * @return 信息
     */
    @ExceptionHandler(value = RunRequestException.class)
    public RestResult badRequestException(RunRequestException badRequestException) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(badRequestException));
        return RestResultUtil.error(badRequestException.getStatus(), badRequestException.getMessage());
    }

    /**
     * 无权访问异常处理
     *
     * @param accessDeniedException 异常
     * @return 信息
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public RestResult noAccessException(AccessDeniedException accessDeniedException) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(accessDeniedException));
        return RestResultUtil.error(User_NO_Access.getCode(), User_NO_Access.getMessage());
    }

    /**
     * 统一处理Service层异常
     *
     * @param service 异常
     * @return 信息
     */
    @ExceptionHandler(value = ServiceException.class)
    public RestResult handlerServiceException(ServiceException service) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(service));
        return RestResultUtil.error(service.getStatus(), service.getMessage());
    }
}
