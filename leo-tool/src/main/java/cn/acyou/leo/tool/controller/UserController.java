package cn.acyou.leo.tool.controller;


import cn.acyou.leo.framework.annotation.authz.RequiresRoles;
import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.dto.req.UserLoginAccountReq;
import cn.acyou.leo.tool.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author youfang
 * @since 2022-08-16
 */
@RestController
@Api(tags = "用户")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录")
    public Result<?> login(@Validated @RequestBody UserLoginAccountReq loginAuth) {
        return userService.doLoginValid(loginAuth);
    }

    @PostMapping("/loginInfo")
    @ApiOperation(value = "获取当前登录用户详细信息")
    public Result<LoginUser> loginInfo() {
        LoginUser loginUser = AppContext.getLoginUser();
        return Result.success(loginUser);
    }

}
