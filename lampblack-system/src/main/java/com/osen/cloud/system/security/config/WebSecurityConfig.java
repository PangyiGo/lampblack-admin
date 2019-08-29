package com.osen.cloud.system.security.config;

import com.osen.cloud.system.security.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 16:30
 * Description: 安全框架核心配置
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // 未登陆时返回 JSON 格式的数据给前端（否则为 html）
    @Autowired
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    // 登录成功返回的 JSON 格式数据给前端（否则为 html）
    @Autowired
    private UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

    // 登录失败返回的 JSON 格式数据给前端（否则为 html）
    @Autowired
    private UserAuthenticationFailureHandler userAuthenticationFailureHandler;

    // 注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;

    // 无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 密码加密
     *
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这里记住一定要重新父类的对象，不然在注入 AuthenticationManager时会找不到
     *
     * @return 返回
     * @throws Exception 异常
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义认证配置
     *
     * @param auth 处理器
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 这块是配置跨域请求的
     *
     * @return 返回
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.addAllowedOrigin("*");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", cors);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用csrf
                .csrf().disable()

                // 不创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // 授权异常
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint).and()

                // 无权访问
                .exceptionHandling().accessDeniedHandler(userAccessDeniedHandler).and()

                // 表单
                .formLogin()
                .loginPage("/**/auth/login")
                .successHandler(userAuthenticationSuccessHandler)
                .failureHandler(userAuthenticationFailureHandler)
                .permitAll()

                .and()

                .logout()
                .logoutUrl("/**/auth/logout")
                .logoutSuccessHandler(userLogoutSuccessHandler)
                .permitAll()
                .and()
                // 过滤请求
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).anonymous()

                // swagger start
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                // swagger end

                // 其他接口认证
                .anyRequest().authenticated()

                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable()

                .and().headers().cacheControl();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
