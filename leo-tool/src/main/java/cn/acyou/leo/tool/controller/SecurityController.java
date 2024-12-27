package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

/**
 * @author youfang
 * @version [1.0.0, 2020/6/29]
 **/
@RestController
@RequestMapping("demo")
@Api(tags = "SpringSecurity权限校验示例")
public class SecurityController {

    @GetMapping(value = "/page")
    @PreAuthorize("permitAll()")        //方法一：
    @PermitAll                          //方法二：
    public Result<?> page() {
        return Result.success("ok");
    }

    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('demo:list')")
    public Result<?> list() {
        System.out.println("拥有demo:list 权限才能访问成功！");
        return Result.success("ok");
    }

    @GetMapping(value = "/get")
    @PreAuthorize("hasAuthority('demo:get')")
    public Result<?> get() {
        System.out.println("拥有demo:get 权限才能访问成功！");
        return Result.success("ok");
    }

    @GetMapping(value = "/rolePermission")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> roleTest() {
        System.out.println("拥有Admin角色才能访问成功！");
        return Result.success("ok");
    }

}
