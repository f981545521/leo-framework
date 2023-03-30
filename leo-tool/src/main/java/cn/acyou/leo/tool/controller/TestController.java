package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.service.ParamConfigService;
import cn.acyou.leo.tool.service.common.AsyncService;
import cn.acyou.leo.tool.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
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
    /**
     * Resource注解的主要属性：
     * name：指定需注入的bean的名称
     * type： 指定需注入的bean的类型
     * 1. 如果不写，默认回到ByType匹配
     * 2. 如果写name，会根据name匹配
     * 3. 如果写name+type，会根据name和type匹配
     * 源码：
     * {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor#fallbackToDefaultTypeMatch}
     */
    @Resource
    private CommonService commonService8888;
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

    @ApiOperation("测试Post大小")
    @PostMapping("testPostSize")
    public Result<Void> testPostSize(@RequestBody List<Object> res) {
        System.out.println(res.size());
        return Result.success();
    }

    @ApiOperation("测试异常")
    @GetMapping("testException")
    public Result<Void> testException() {
        int i = 1 / 0;
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
        commonService8888.testRetry(key);
        return Result.success();
    }

    @ApiOperation("测试Service的同步方法")
    @GetMapping("testSynchronized")
    public Result<Void> testSynchronized(DictVo dictVo) {
        commonService8888.testSynchronized(dictVo);
        return Result.success();
    }

    @ApiOperation("请求并保持 一直到返回结果(非null)")
    @GetMapping("getWithKeep")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "waitTime", value = "等待时间", required = true, dataType = "long", example = "3000"),
            @ApiImplicitParam(name = "interval", value = "执行间隔", required = true, dataType = "long", example = "1000")
    })
    public Result<String> getWithKeep(long waitTime, long interval) {
        String res = commonService8888.getWithKeep(waitTime, interval);
        return Result.success(res);
    }


    @ApiOperation(value = "测试GET传时间参数", notes = "需要使用：@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")")
    @GetMapping("testGetDate")
    public Result<Void> testGetDate(DictVo dictVo) {
        log.info("GET:{}", dictVo);
        return Result.success();
    }
}
