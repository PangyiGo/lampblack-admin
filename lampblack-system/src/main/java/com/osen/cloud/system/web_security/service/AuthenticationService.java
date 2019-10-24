package com.osen.cloud.system.web_security.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.system.web_security.utils.JwtTokenUtil;
import com.osen.cloud.system.web_security.utils.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * User: PangYi
 * Date: 2019-08-30
 * Time: 9:13
 * Description: 验证服务类
 */
@Service
public class AuthenticationService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 刷新token值
     *
     * @param authorization 令牌
     * @return 信息
     */
    @Transactional(rollbackFor = Exception.class)
    public String refreshToken(String authorization) {
        String token = authorization.substring(7);
        // 获取旧token
        JwtUser jwtUser = JSON.parseObject(stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + token).get(), JwtUser.class);
        if (BeanUtil.isEmpty(jwtUser))
            return null;
        // 验证token
        String refresh = null;
        if (jwtTokenUtil.validateToken(token, jwtUser)) {
            refresh = jwtTokenUtil.refreshToken(token);
            // 清除旧token
            stringRedisTemplate.delete(JwtTokenUtil.KEYS + token);
            // 重新保存
            stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + refresh).set(JSON.toJSONString(jwtUser), JwtTokenUtil.EXPIRATION,
                    TimeUnit.MILLISECONDS);
        }
        return refresh;
    }
}
