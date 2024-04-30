package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.service.UserTokenService;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.dto.req.UserLoginAccountReq;
import cn.acyou.leo.tool.entity.User;
import cn.acyou.leo.tool.service.UserService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-08-16
 */
@Service
public class UserServiceImpl implements UserService, UserTokenService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private List<User> dbUserList;


    @Override
    public LoginUser getLoginUserByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = dbUserList.stream().filter(x -> x.getUserId().equals(userId)).findFirst().orElse(null);
        if (user == null) {
            throw new ServiceException("用户不存在！");
        }
        return prepareLoginUser(AppContext.getToken(), user);
    }

    @Override
    public Result<?> doLoginValid(UserLoginAccountReq loginAuth) {
        return Result.success(loginByAccount(loginAuth));
    }

    @Override
    public LoginUser prepareLoginUser(String token, User user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        loginUser.setUserId(user.getUserId());
        loginUser.setUserName(user.getUserName());
        if (user.getRoleId().equals(Constant.CONS_LONG_1)) {
            loginUser.setRoleCodes(Sets.newHashSet("ADMIN"));
        }
        if (user.getRoleId().equals(Constant.CONS_LONG_2)) {
            loginUser.setRoleCodes(Sets.newHashSet("USER"));
        }
        if (user.getPerms() != null) {
            loginUser.setPermsList(Sets.newHashSet(user.getPerms().split(",")));
        }
        return loginUser;
    }

    /**
     * 账号（手机号）密码登录
     *
     * @param accountLogin 账户登录
     * @return {@link LoginUser}
     */
    private LoginUser loginByAccount(UserLoginAccountReq accountLogin) {
        String phone = accountLogin.getPhone();
        String password = accountLogin.getPassword();
        User accountUser = dbUserList.stream().filter(x -> x.getPhone().equals(phone)).findFirst().orElse(null);
        if (accountUser == null) {
            throw new ServiceException("用户名或密码错误，请检查！");
        }
        String s = redisUtils.get(RedisKeyConstant.USER_LOGIN_LOCK + accountUser.getUserId());
        if (!password.equals(accountUser.getPassword())) {
            throw new ServiceException("用户名或密码错误，请检查！");
        }
        if (Constant.DISABLED.equals(accountUser.getStatus())) {
            throw new ServiceException("账号被禁用，请联系管理员激活！");
        }
        long timeOut = 30 * 24 * 60 * 60;
        String token = RandomUtil.randomUuidWithoutLine();
        String clientType = AppContext.getClientType().getMessage();
        String userLoginIdKey = RedisKeyConstant.USER_LOGIN_ID + clientType + ":" + accountUser.getUserId().toString();

        //登录的时候、存在TOKEN
        String existToken = redisUtils.get(userLoginIdKey);
        if (StringUtils.isNotEmpty(existToken)) {
            redisUtils.set(RedisKeyConstant.USER_LOGIN_AT_OTHER_WHERE + existToken,
                    accountUser.getUserId().toString(),
                    redisUtils.getExpire(RedisKeyConstant.USER_LOGIN_TOKEN + existToken), TimeUnit.SECONDS);
            //删除之前登陆的TOKEN
            redisUtils.delete(RedisKeyConstant.USER_LOGIN_TOKEN + existToken);
        }
        //添加用户信息
        redisUtils.set(RedisKeyConstant.USER_LOGIN_TOKEN + token, accountUser.getUserId().toString(), timeOut, TimeUnit.SECONDS);
        redisUtils.set(userLoginIdKey, token, timeOut, TimeUnit.SECONDS);
        redisUtils.set(RedisKeyConstant.USER_LOGIN_INFO + accountUser.getUserId().toString(), JSON.toJSONString(accountUser), timeOut, TimeUnit.SECONDS);
        return prepareLoginUser(token, accountUser);
    }

}
