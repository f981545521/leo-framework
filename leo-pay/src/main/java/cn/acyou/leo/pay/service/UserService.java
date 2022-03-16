package cn.acyou.leo.pay.service;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.service.UserTokenService;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2022-3-16]
 **/
@Service
public class UserService implements UserTokenService {
    /**
     * 根据userId获取登录用户
     *
     * @param userId 用户ID
     * @return {@link LoginUser}
     */
    @Override
    public LoginUser getLoginUserByUserId(Long userId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setUserName("张飞");
        return loginUser;
    }
}
