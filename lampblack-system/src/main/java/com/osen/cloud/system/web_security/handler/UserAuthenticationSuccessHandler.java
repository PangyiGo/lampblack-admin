package com.osen.cloud.system.web_security.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.system.system_logging.util.OperationLogsUtil;
import com.osen.cloud.system.web_security.utils.JwtTokenUtil;
import com.osen.cloud.system.web_security.utils.JwtUser;
import com.osen.cloud.system.web_security.utils.TransferUserToJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.osen.cloud.common.enums.InfoMessage.User_Login_Success;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:07
 * Description: 用户登录成功时，返回给前端数据
 */
@Component
@Slf4j
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OperationLogsUtil operationLogsUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("用户登录成功");

        // 登录成功，逻辑处理代码....
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        //生成token
        String token = jwtTokenUtil.generateToken(jwtUser);

        TransferUserToJwt transferUserToJwt = JwtTokenUtil.toUser(jwtUser);

        // 保存用户登录操作信息
        operationLogsUtil.handlerOperation(request, "用户登录成功", transferUserToJwt.getUsername());

        //保存redis
        String jsonString = JSON.toJSONString(transferUserToJwt);
        stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + token).set(jsonString, JwtTokenUtil.EXPIRATION, TimeUnit.MILLISECONDS);

        response.setContentType("application/json;charset=utf-8");

        // 返回信息体
        String res = JSON.toJSONString(RestResultUtil.authorization(User_Login_Success.getCode(), token));

        response.getWriter().write(res);
    }
}
