package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author youfang
 * @version [1.0.0, 2020/9/2]
 **/
@Slf4j
@RestController
@RequestMapping("/main")
@Api(value = "MainController", tags = "从此开始")
public class MainController {

    @GetMapping(value = "/start")
    @ApiOperation("测试一下")
    public Result<String> test1(String key) {
        return Result.success(key);
    }

}
