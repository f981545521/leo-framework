package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youfang
 * @version [1.0.0, 2020/6/29]
 **/
@RestController
@RequestMapping("order")
public class OrderController {

    @RequestMapping(value = "/get")
    @PreAuthorize("hasAuthority('order:get')")
    public Result<?> get() {
        System.out.println("order:get 权限才能访问成功！");
        return Result.success(SecurityUtils.getAuthentication());
    }

}
