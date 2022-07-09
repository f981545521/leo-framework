package cn.acyou.leo.pay.controller;

import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.pay.dto.TestBooleanVo;
import cn.acyou.leo.pay.dto.TestDestroyBean;
import cn.acyou.leo.pay.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("测试RedisLock注解使用")
    public Result<String> test1(String key) {
        String s = commonService.testRedisLock(key);
        return Result.success(s);
    }

    @PostMapping(value = "/testBoolean")
    @ApiOperation("测试Boolean")
    public Result<TestBooleanVo> testBoolean(@RequestBody TestBooleanVo vo) {
        return Result.success(vo);
    }

    @RequestMapping(value = "/testDestroyBean", method = {RequestMethod.GET})
    @ApiOperation("测试使用 SpringHelper.createBean 创建的对象可以被GC回收")
    public Result<String> testDestroyBean(int count) {
        System.out.println(SpringHelper.getSingletonCount());
        AsyncManager.execute(() -> {
            for (int i = 0; i < count; i++) {
                SpringHelper.createBean(TestDestroyBean.class);
                WorkUtil.trySleep(3000);
                System.out.println(SpringHelper.getSingletonCount());
            }
        });
        return Result.success("ok");
    }

}
