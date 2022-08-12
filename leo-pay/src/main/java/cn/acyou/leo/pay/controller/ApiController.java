package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 13:55]
 **/
@Slf4j
@RestController
@RequestMapping("/api")
@Api(value = "API服务接口", tags = "API服务接口")
public class ApiController {

    @RequestMapping(value = "/test1", method = {RequestMethod.GET})
    @ApiOperation("test1")
    public Result<Void> test1() {
        return Result.success();
    }

    @RequestMapping(value = "/test2", method = {RequestMethod.GET})
    @ApiOperation("test2")
    public Result<List<LoginUser>> test2() {
        List<LoginUser> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(Long.valueOf("1000" + i));
            loginUser.setUserName("XXX" + i + "号");
            list.add(loginUser);
        }
        return Result.success(list);
    }


}
