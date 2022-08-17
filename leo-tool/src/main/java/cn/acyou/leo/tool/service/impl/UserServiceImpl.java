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
import cn.acyou.leo.tool.mapper.UserMapper;
import cn.acyou.leo.tool.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserTokenService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public LoginUser getLoginUserByUserId(Long userId) {
        User user = getById(userId);
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
        loginUser.setRoleCodes(Sets.newHashSet(String.valueOf(user.getRoleId())));
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
        User accountUser = lambdaQuery().eq(User::getPhone, phone).one();
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
