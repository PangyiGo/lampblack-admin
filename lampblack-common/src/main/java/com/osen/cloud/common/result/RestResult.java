package com.osen.cloud.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 18:37
 * Description: 统一返回个税
 */
@Data
@NoArgsConstructor
public class RestResult<T> {

    private Integer code;

    private String message;

    private T data;

    public RestResult(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public RestResult(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }
}
