package com.osen.cloud.common.except;

import com.osen.cloud.common.except.type.BadRequestException;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.ThrowableUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.osen.cloud.common.enums.InfoMessage.NoFound_Error;
import static com.osen.cloud.common.enums.InfoMessage.UnknownSystem_Error;
import static com.osen.cloud.common.enums.InfoMessage.User_Login_Guoqi;

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
    @ExceptionHandler(Throwable.class)
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
    @ExceptionHandler(NotFoundException.class)
    public RestResult notfound(NotFoundException noFound) {
        // 打印错误日志
        log.error(ThrowableUtil.getStackTrace(noFound));
        return RestResultUtil.error(NoFound_Error.getCode(), NoFound_Error.getMessage());
    }

    /**
     * 处理自定义异常
     *
     * @param bad
     * @return
     */
    @ExceptionHandler(value = BadRequestException.class)
    public RestResult badRequestException(BadRequestException bad) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(bad));
        return RestResultUtil.error(User_Login_Guoqi.getCode(), User_Login_Guoqi.getMessage());
    }
}
