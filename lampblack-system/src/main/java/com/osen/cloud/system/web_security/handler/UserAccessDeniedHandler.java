package com.osen.cloud.system.web_security.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.utils.RestResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.cloud.common.enums.InfoMessage.User_NO_Access;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:04
 * Description: 用户无权访问
 */
@Component
@Slf4j
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("用户无权访问");
        response.setContentType("application/json;charset=utf-8");
        // 返回信息体
        String res = JSON.toJSONString(RestResultUtil.authorization(User_NO_Access.getCode(),
                User_NO_Access.getMessage()));
        response.getWriter().write(res);
    }
}
