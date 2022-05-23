package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.service.common.AsyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/16 13:43]
 **/
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("测试异步缓存")
    @GetMapping("test")
    public Result<Void> test() {
        asyncService.printOk();
        return Result.success();
    }

    @ApiOperation("测试访问限制（编码实现）")
    @GetMapping("accessLimit")
    public Result<Void> accessLimit(String key) {
        redisUtils.accessLimit(key, 5000);
        return Result.success();
    }

    @ApiOperation("测试访问限制（注解实现）")
    @GetMapping("accessLimit2")
    @AccessLimit(interval = 5000, value = "#key")
    public Result<Void> accessLimit2(String key) {
        return Result.success();
    }


}
