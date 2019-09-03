package com.osen.cloud.system.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 10:39
 * Description: jwt token令牌工具类
 */
@Component
public class JwtTokenUtil {

    // 授权头
    public static final String TOKEN_HEADER = "Authorization";

    // 授权前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    // 加密密钥
    private static final String SECRET = "osen";

    public static final String KEYS = "username:access_token:";

    // 有效时间，默认3小时
    public static final long EXPIRATION = 60 * 60 * 6 * 1000;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token   令牌
     * @param jwtUser 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, JwtUser jwtUser) {
        String username = getUsernameFromToken(token);
        return (username.equals(jwtUser.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 将认证信息转换为序列化
     *
     * @param jwtUser 结构体
     * @return 信息
     */
    public static TransferUserToJwt toUser(JwtUser jwtUser) {
        TransferUserToJwt transferUserToJwt = new TransferUserToJwt();
        transferUserToJwt.setId(jwtUser.getId());
        transferUserToJwt.setUsername(jwtUser.getUsername());
        transferUserToJwt.setPassword(jwtUser.getPassword());
        List<String> list = new ArrayList<>();
        for (GrantedAuthority authority : jwtUser.getAuthorities()) {
            SimpleGrantedAuthority simpleGrantedAuthority = (SimpleGrantedAuthority) authority;
            list.add(simpleGrantedAuthority.getAuthority());
        }
        transferUserToJwt.setAuthority(list);
        return transferUserToJwt;
    }

    /**
     * 将序列化转换为认证信息
     *
     * @param transferUserToJwt 序列化
     * @return 信息
     */
    public static JwtUser toJwt(TransferUserToJwt transferUserToJwt) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(transferUserToJwt.getId());
        jwtUser.setUsername(transferUserToJwt.getUsername());
        jwtUser.setPassword(transferUserToJwt.getPassword());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (String auth : transferUserToJwt.getAuthority()) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth);
            authorityList.add(simpleGrantedAuthority);
        }
        jwtUser.setAuthorities(authorityList);
        return jwtUser;
    }
}
