package com.osen.cloud.system.security.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtils;
import com.osen.cloud.system.security.utils.JwtTokenUtil;
import com.osen.cloud.system.security.utils.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 令牌刷新
     *
     * @param authorization token
     * @return 信息
     */
    @PostMapping("/auth/refresh")
    public RestResult restResult(@RequestHeader("Authorization") String authorization) {
        log.info("user refresh token: " + SecurityUtils.getUsername());
        String token = authorization.substring(7);
        // 获取旧token
        JwtUser jwtUser = JSON.parseObject(stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + token).get(), JwtUser.class);
        if (BeanUtil.isEmpty(jwtUser))
            return RestResultUtil.authorization(User_Login_Guoqi.getCode(), User_Login_Guoqi.getMessage());
        // 验证token
        String refresh = null;
        boolean isOk = false;
        if (jwtTokenUtil.validateToken(token, jwtUser)) {
            refresh = jwtTokenUtil.refreshToken(token);
            // 清楚旧token
            isOk = stringRedisTemplate.delete(JwtTokenUtil.KEYS + token);
        }
        if (isOk) {
            // 重新保存
            stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + refresh).set(refresh, JwtTokenUtil.EXPIRATION, TimeUnit.MILLISECONDS);
            return RestResultUtil.authorization(Refresh_OK.getCode(), refresh);
        } else {
            return RestResultUtil.authorization(User_Login_Guoqi.getCode(), User_Login_Guoqi.getMessage());
        }

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
        if (delete) {
            return RestResultUtil.authorization(User_Logout_Success.getCode(), User_Logout_Success.getMessage());
        } else {
            return RestResultUtil.authorization(User_Logout_Failed.getCode(), User_Logout_Failed.getMessage());
        }

    }
}
