package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.security.SecurityUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2020/6/30]
 **/
@Slf4j
@RestController
@RequestMapping("/sys/auth")
public class SystemController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/login")
    public Result<LoginUser> login(String username, String password) {
        System.out.println("用户执行登录：username:" + username);
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("授权通过");
        }catch (Exception e) {
            if (e instanceof BadCredentialsException){
                throw new ServiceException("用户名或密码错误，请检查！");
            }else if (e instanceof DisabledException){
                throw new ServiceException("用户已失效，请检查！");
            }else if (e instanceof AccountExpiredException){
                throw new ServiceException("用户帐号已过期，请检查！");
            }else if (e instanceof LockedException){
                throw new ServiceException("用户帐号已被锁定，请检查！");
            }else {
                throw new ServiceException("登录失败，请检查！");
            }
        }
        // 用户信息
        UserDetails user = (UserDetails) authentication.getPrincipal();
        System.out.println(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //登录后
        Authentication afterAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(afterAuth);

        //Token
        String token = UUID.randomUUID().toString();

        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        loginUser.setUserId(100L);
        loginUser.setUserName(user.getUsername());
        loginUser.setPermsList(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

        long timeOut = 30 * 24 * 60 * 60;
        String clientType = AppContext.getClientType().getMessage();
        String userLoginIdKey = RedisKeyConstant.USER_LOGIN_ID + clientType + ":" + loginUser.getUserId().toString();
        //登录的时候、存在TOKEN
        String existToken = redisUtils.get(userLoginIdKey);
        if (StringUtils.isNotEmpty(existToken)) {
            redisUtils.set(RedisKeyConstant.USER_LOGIN_AT_OTHER_WHERE + existToken, loginUser.getUserId().toString(), redisUtils.getExpire(RedisKeyConstant.USER_LOGIN_TOKEN + existToken), TimeUnit.SECONDS);
            //删除之前登陆的TOKEN
            redisUtils.delete(RedisKeyConstant.USER_LOGIN_TOKEN + existToken);
        }
        //添加用户信息
        redisUtils.set(RedisKeyConstant.USER_LOGIN_TOKEN + token, loginUser.getUserId().toString(), timeOut, TimeUnit.SECONDS);
        redisUtils.set(userLoginIdKey, token, timeOut, TimeUnit.SECONDS);
        redisUtils.set(RedisKeyConstant.USER_LOGIN_INFO + loginUser.getUserId().toString(), JSON.toJSONString(loginUser), timeOut, TimeUnit.SECONDS);

        return Result.success(loginUser);
    }

    @GetMapping("/userInfo")
    public Result<?> userInfo() {
        Authentication afterAuth = SecurityContextHolder.getContext().getAuthentication();
        log.info("登录用户信息：{}", afterAuth);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return Result.success(loginUser);
    }
}
