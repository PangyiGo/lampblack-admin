package com.osen.cloud.system.security.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.service.authorization.AuthorizationService;
import com.osen.cloud.system.security.utils.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 15:48
 * Description: 用户授权服务层接口实现类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 根据用户名验证用户是否有效
     *
     * @param username 用户名
     * @return 认证
     * @throws UsernameNotFoundException 异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authorizationService.findByUsername(username);
        if (BeanUtil.isEmpty(user) || StringUtils.isEmpty(user.getAccount()))
            throw new UsernameNotFoundException("账号不存在");
        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("账号不可用");
        }
        // 封装用户对应角色
        List<SimpleGrantedAuthority> authorityList = user.getRoles().stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new JwtUser(user.getId(), user.getAccount(), user.getPassword(), authorityList);
    }
}
