package cn.acyou.leo.tool.security;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.util.StringUtils;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotBlank(token)) {
            String userId = redisUtils.get(RedisKeyConstant.USER_LOGIN_TOKEN + token);
            if (userId != null) {
                LoginUser loginUser = JSON.parseObject(redisUtils.get(RedisKeyConstant.USER_LOGIN_INFO + userId), LoginUser.class);
                Set<SimpleGrantedAuthority> collect = loginUser.getPermsList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, collect);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
