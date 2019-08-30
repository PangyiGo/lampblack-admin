package com.osen.cloud.system.security.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.common.except.type.RunRequestException;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtils;
import com.osen.cloud.system.security.service.AuthenticationService;
import com.osen.cloud.system.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.osen.cloud.common.enums.InfoMessage.*;

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
    private AuthenticationService authenticationService;

    /**
     * 令牌刷新
     *
     * @param authorization token
     * @return 信息
     */
    @PostMapping("/auth/refresh")
    public RestResult restResult(@RequestHeader("Authorization") String authorization) {
        log.info("user refresh token: " + SecurityUtils.getUsername());
        String refresh = authenticationService.refreshToken(authorization);
        if (StringUtils.isEmpty(refresh))
            throw new RunRequestException(Refresh_Failed.getCode(), Refresh_Failed.getMessage());
        return RestResultUtil.authorization(Refresh_OK.getCode(), Refresh_OK.getMessage());
    }

    /**
     * 用户退出登录
     *
     * @param authorization token
     * @return 信息
     */
    @PostMapping("/auth/logout")
    public RestResult logout(@RequestHeader("Authorization") String authorization) {
        log.info("user logout: " + authorization);
        // 清除redis缓存
        boolean delete = stringRedisTemplate.delete(JwtTokenUtil.KEYS + authorization.substring(7));
        if (delete)
            throw new RunRequestException(User_Logout_Success.getCode(), User_Logout_Success.getMessage());
        return RestResultUtil.authorization(User_Logout_Failed.getCode(), User_Logout_Failed.getMessage());
    }
}
