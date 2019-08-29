package com.osen.cloud.system.security.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 17:41
 * Description: 认证控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
public class AuthorizationController {

    @GetMapping("/auth/refresh")
    public RestResult restResult() {
        System.out.println(SecurityUtils.getUserId());
        return RestResultUtil.success();
    }

}
