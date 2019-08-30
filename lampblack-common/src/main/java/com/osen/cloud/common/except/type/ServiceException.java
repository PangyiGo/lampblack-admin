package com.osen.cloud.common.except.type;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 18:41
 * Description: 统一异常处理程序
 */
@Getter
@NoArgsConstructor
public class ServiceException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.status = code;
    }
}
