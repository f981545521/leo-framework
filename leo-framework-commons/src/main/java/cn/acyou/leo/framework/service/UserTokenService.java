package cn.acyou.leo.framework.service;

import cn.acyou.leo.framework.base.LoginUser;

/**
 * @author youfang
 * @version [1.0.0, 2022-3-16]
 **/
public interface UserTokenService {
    /**
     * 根据userId获取登录用户
     *
     * @param userId 用户ID
     * @return {@link LoginUser}
     */
    LoginUser getLoginUserByUserId(Long userId);
}
