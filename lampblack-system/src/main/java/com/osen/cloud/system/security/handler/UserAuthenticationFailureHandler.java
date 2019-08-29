package com.osen.cloud.system.security.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.utils.RestResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.cloud.common.enums.InfoMessage.User_Login_Failed;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:11
 * Description: 用户登录失败，返回给前端数据
 */
@Component
@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("用户登录失败");

        // 登录失败，逻辑处理代码....

        response.setContentType("application/json;charset=utf-8");
        // 返回信息体
        String res = JSON.toJSONString(RestResultUtil.authorization(User_Login_Failed.getCode(),
                User_Login_Failed.getMessage()));
        response.getWriter().write(res);
    }
}
