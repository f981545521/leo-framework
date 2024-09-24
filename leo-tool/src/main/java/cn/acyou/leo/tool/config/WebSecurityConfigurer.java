package cn.acyou.leo.tool.config;

import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.SourceUtil;
import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.framework.util.StringUtils;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.security.AuthenticationTokenFilter;
import cn.acyou.leo.tool.security.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * token认证过滤器
     */
    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 获取匿名标记
        Set<String> anonymousUrls = getAnonymousUrl();
        // 认证失败处理类
        httpSecurity.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            log.error("账号认证失败：{}", authException.getMessage());
            String token = SecurityUtils.getToken(request);
            //认证失败，禁止访问！
            //携带token访问认证失败，默认提示登录已过期
            Result<Object> error = Result.error(CommonErrorEnum.E_LOGIN_TIMEOUT);
            if (StringUtils.isBlank(token)) {
                error = Result.error(CommonErrorEnum.E_UNAUTHENTICATED);
            }else {
                String loginAtOtherWhere = redisUtils.get(RedisKeyConstant.USER_LOGIN_AT_OTHER_WHERE + token);
                if (StringUtils.isNotBlank(loginAtOtherWhere)) {
                    error = Result.error(CommonErrorEnum.E_LOGIN_AT_OTHER_WHERE);
                }
            }
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(error));
        });
        //httpSecurity.exceptionHandling().accessDeniedHandler();//这个无法走，通过 {@link AccessDeniedExceptionHandler} 来处理
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable().and()
                // 基于token，所以不需要session 去除JSESSIONID
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 允许匿名访问
                .antMatchers("/sys/auth/login", "/sys/auth/logout").permitAll()
                .antMatchers("/oauth/*", "/login").permitAll()
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                // 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                .antMatchers("/doc.html", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        // 添加Logout
        httpSecurity.logout().logoutUrl("/sys/auth/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String token = SecurityUtils.getToken(request);
                String userId = redisUtils.get(RedisKeyConstant.USER_LOGIN_TOKEN + token);
                if (StringUtils.isNotBlank(userId)) {
                    redisUtils.delete(RedisKeyConstant.USER_LOGIN_TOKEN + token);
                    redisUtils.delete(RedisKeyConstant.USER_LOGIN_ID + SourceUtil.getClientTypeByUserAgent(request) + ":" + userId);
                }
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(Result.success()));
            }
        });
        // 添加Token filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @ControllerAdvice
    public static class AccessDeniedExceptionHandler{
        @ExceptionHandler(value = AccessDeniedException.class)
        @ResponseBody
        public Result<String> exceptionHandler(HttpServletRequest req, AccessDeniedException e){
            log.error("权限认证失败：{}", e.getMessage());
            return Result.error(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
    }


    private Set<String> getAnonymousUrl() {
        // 搜寻匿名标记 url
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringHelper.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>(8);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            PermitAll permitAll = handlerMethod.getMethodAnnotation(PermitAll.class);
            if (null != preAuthorize && infoEntry.getKey().getPatternsCondition() != null) {
                if ("permitAll".equals(preAuthorize.value()) || "permitAll()".equals(preAuthorize.value())) {
                    anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                }
            }
            if (permitAll != null && infoEntry.getKey().getPatternsCondition() != null){
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return anonymousUrls;
    }

}

