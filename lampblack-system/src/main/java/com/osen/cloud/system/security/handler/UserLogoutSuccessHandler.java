package com.osen.cloud.system.security.handler;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.utils.RestResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.cloud.common.enums.InfoMessage.User_Logout_Success;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:13
 * Description: 用户退出成功，返回给前端数据
 */
@Component
@Slf4j
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("用户退出登录成功");

        // 用户退出登录成功，逻辑处理代码....

        response.setContentType("application/json;charset=utf-8");
        // 返回信息体
        String res = JSON.toJSONString(RestResultUtil.authorization(User_Logout_Success.getCode(),
                User_Logout_Success.getMessage()));
        response.getWriter().write(res);
    }
}
