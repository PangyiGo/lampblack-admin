package com.osen.cloud.system.web_security.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.system.web_security.utils.JwtTokenUtil;
import com.osen.cloud.system.web_security.utils.JwtUser;
import com.osen.cloud.system.web_security.utils.TransferUserToJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 16:01
 * Description: 认证过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求头
        String header = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        String username = null;
        String authToken = null;
        if (StringUtils.isNotEmpty(header) && header.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            authToken = header.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (Exception e) {
                log.error("Token parse failed");
            }
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 从数据库获取用户信息
            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(username);

            // if (stringRedisTemplate.hasKey(JwtTokenUtil.KEYS + authToken)) {
            //     // 从 redis 缓存中获取认证主体
            //     String json = stringRedisTemplate.boundValueOps(JwtTokenUtil.KEYS + authToken).get();
            //
            //     TransferUserToJwt transferUserToJwt = JSON.parseObject(json, TransferUserToJwt.class);
            //
            //     JwtUser userDetails = JwtTokenUtil.toJwt(transferUserToJwt);

            if (userDetails != null) {
                // 验证token是否有效
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            // }
        }

        filterChain.doFilter(request, response);
    }
}
