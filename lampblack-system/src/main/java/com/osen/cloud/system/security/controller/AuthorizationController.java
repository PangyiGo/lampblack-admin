package com.osen.cloud.system.security.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtils;
import com.osen.cloud.system.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.osen.cloud.common.enums.InfoMessage.User_Logout_Failed;
import static com.osen.cloud.common.enums.InfoMessage.User_Logout_Success;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 17:41
 * Description: 认证控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class AuthorizationController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/auth/refresh")
    public RestResult restResult() {
        System.out.println(SecurityUtils.getUserId());
        return RestResultUtil.success();
    }

    /**
     * 用户退出登录
     *
     * @param authorization token
     * @return 信息
     */
    @PostMapping("/auth/logout")
    public RestResult logout(@RequestHeader("Authorization") String authorization) {

        log.info("user logout" + authorization);

        boolean delete = stringRedisTemplate.delete(JwtTokenUtil.KEYS + authorization.substring(7));
        if (delete) {
            return RestResultUtil.authorization(User_Logout_Success.getCode(), User_Logout_Success.getMessage());
        } else {
            return RestResultUtil.authorization(User_Logout_Failed.getCode(), User_Logout_Failed.getMessage());
        }

    }
}
