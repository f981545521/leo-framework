package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.model.Result;
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

    @ApiOperation("测试异步缓存")
    @GetMapping("test")
    public Result<Void> test() {
        asyncService.printOk();
        return Result.success();
    }


}
