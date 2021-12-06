package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author youfang
 * @version [1.0.0, 2020/9/2]
 **/
@Slf4j
@RestController
@RequestMapping("/main")
@Api(value = "MainController", tags = "从此开始")
public class MainController {

    @RequestMapping(value = "/start", method = {RequestMethod.GET})
    @ResponseBody
    @ApiOperation("MainController test1")
    public Result<String> test1(String key) {
        return Result.success(key);
    }

}
