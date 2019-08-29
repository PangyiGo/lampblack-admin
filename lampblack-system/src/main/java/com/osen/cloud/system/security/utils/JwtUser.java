package com.osen.cloud.system.security.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 10:29
 * Description: 授权认证实体对象
 */
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails, Serializable {

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账号是否未过期，默认是false
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否未锁定，默认是false
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账号凭证是否未过期，默认是false
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号是否可用，默认是false
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
