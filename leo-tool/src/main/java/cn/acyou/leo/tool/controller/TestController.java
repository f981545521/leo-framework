package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.service.ParamConfigService;
import cn.acyou.leo.tool.service.common.AsyncService;
import cn.acyou.leo.tool.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/16 13:43]
 **/
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
@Slf4j
public class TestController {
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation("测试异步缓存")
    @GetMapping("test")
    public Result<Void> test() {
        asyncService.printOk();
        return Result.success();
    }

    @ApiOperation("测试异步缓存2")
    @GetMapping("test2")
    public Result<Void> test2() {
        AsyncManager.execute(() -> {
            log.info("HELLO");
        });
        AsyncManager.schedule(() -> {
            log.info("HELLO");
        });
        Future<String> submit = AsyncManager.submit(() -> {
            return "GOGO";
        });
        return Result.success();
    }

    @ApiOperation("测试异步 有结果")
    @GetMapping("test3")
    public Result<String> test3() throws Exception {
        Future<String> result = asyncService.getResult();
        return Result.success(result.get());
    }

    @ApiOperation("测试异步缓存 AsyncService")
    @GetMapping("testAsyncService")
    public Result<Void> test(String name) {
        asyncService.exec(name);
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

    @ApiOperation("测试访问限制（注解实现3）")
    @GetMapping("accessLimit3")
    @AccessLimit(interval = -1, value = "#key")
    public Result<Void> accessLimit3(String key) {
        WorkUtil.trySleep(5000);
        WorkUtil.tryRun(() -> {
            System.out.println("111");
            int i = 1 / 0;
        });
        return Result.success();
    }

    @ApiOperation("测试修改后查询")
    @GetMapping("test223")
    public Result<ParamConfig> test223(@RequestParam Long id, @RequestParam Integer status) {
        ParamConfig db1 = paramConfigService.getById(id);
        db1.setStatus(status);
        paramConfigService.updateById(db1);
        //select
        ParamConfig db2 = paramConfigService.getById(id);
        log.info("test result : {}", db2);
        return Result.success(db2);
    }

    @ApiOperation("测试新增后查询")
    @PostMapping("test224")
    public Result<ParamConfig> test224(@RequestBody ParamConfig param) {
        paramConfigService.saveOrUpdate(param);
        //select
        ParamConfig db2 = paramConfigService.getById(param.getId());
        //结果：能够查出createTime
        log.info("test result : {}", db2);
        return Result.success(db2);
    }

    @ApiOperation("测试在代码中获取请求对象")
    @PostMapping("testReq")
    public Result<ParamConfig> testReq() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return Result.success();
    }

    @ApiOperation("testSql")
    @GetMapping("testSql")
    public Result<List<ParamConfig>> testSql(String sql) {
        List<ParamConfig> res = paramConfigService.selectBySql(sql);
        return Result.success(res);
    }


    @ApiOperation("测试MybatisPlus更新NULL值")
    @GetMapping("testUpdateNull")
    public Result<Void> testUpdateNull(String id, String value) {
        //方法一：
        //boolean update = paramConfigService.update()
        //        .set("value", value)
        //        .set("ext_value", null)
        //        .eq("id", id)
        //        .update();
        //log.info("更新结果：{}", update);

        //方法二：
        //boolean update = paramConfigService.update(new ParamConfig(),
        //        new UpdateWrapper<ParamConfig>().eq("id", id)
        //                .set("value", value)
        //                .set("ext_value", null));
        //log.info("更新结果：{}", update);

        //方法三（推荐）：
        boolean update = paramConfigService.lambdaUpdate()
                .eq(ParamConfig::getId, id)
                .set(ParamConfig::getValue, value)
                .set(ParamConfig::getExtValue, null)
                .update();
        log.info("更新结果：{}", update);
        return Result.success();
    }

    @ApiOperation("测试重试方法")
    @GetMapping("testRetry")
    public Result<Void> testRetry(String key) {
        commonService.testRetry(key);
        return Result.success();
    }
}
