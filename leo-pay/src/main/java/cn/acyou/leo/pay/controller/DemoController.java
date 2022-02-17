package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.pay.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 13:55]
 **/
@Slf4j
@RestController
@RequestMapping("/demo")
@Api(value = "DemoController", tags = "此处用来自测 FrameWork代码")
public class DemoController {
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/start", method = {RequestMethod.GET})
    @ResponseBody
    @ApiOperation("测试RedisLock注解使用")
    public Result<String> test1(String key) {
        String s = commonService.testRedisLock(key);
        return Result.success(s);
    }
}