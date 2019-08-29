package com.osen.cloud.system.security.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.utils.RestResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.cloud.common.enums.InfoMessage.User_Need_Authorization;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 8:54
 * Description: 用户未登录时返回给前端数据
 */
@Component
@Slf4j
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("用户未登录");
        response.setContentType("application/json;charset=utf-8");
        // 返回信息体
        String res = JSON.toJSONString(RestResultUtil.authorization(User_Need_Authorization.getCode(), User_Need_Authorization.getMessage()));
        response.getWriter().write(res);
    }
}
