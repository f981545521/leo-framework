package cn.acyou.leo.tool.service;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.dto.req.UserLoginAccountReq;
import cn.acyou.leo.tool.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-08-16
 */
public interface UserService extends IService<User> {

    Result<?> doLoginValid(UserLoginAccountReq loginAuth);

    /**
     * 准备登陆用户
     *
     * @param user 用户
     * @return LoginUser
     */
    LoginUser prepareLoginUser(String token, User user);
}
