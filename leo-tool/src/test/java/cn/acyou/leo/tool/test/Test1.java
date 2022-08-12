package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.mapper.ParamConfigMapper;
import cn.acyou.leo.tool.retrofit.LeoPayApi;
import cn.acyou.leo.tool.service.ParamConfigService;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 14:14]
 **/
public class Test1 extends ApplicationBaseTests {
    @Autowired
    private ParamConfigService paramConfigService;
    @Autowired
    private ParamConfigMapper paramConfigMapper;
    @Autowired
    private LeoPayApi leoPayApi;

    @Test
    public void tewt2342() {
        Result<JSONObject> aac = leoPayApi.printRequest(1000L, new HashMap<>(), "aac");
        System.out.println(aac);
    }

    @Test
    public void tewt23412() {
        Result<JSONObject> jsonObjectResult = leoPayApi.test1();

        Result<Object> objectResult = leoPayApi.test2();

        System.out.println("ok");
    }

    @Test
    public void test1() {
        boolean b = paramConfigService.removeById(101);
        System.out.println(b);
    }

    @Test
    public void test2() {
        boolean b = paramConfigService.removeByIds(Lists.newArrayList(100, 101));
        System.out.println(b);
    }

    @Test
    public void test4() {
        int i = paramConfigMapper.deleteById(101);
        System.out.println(i);
    }

    @Test
    public void test5() {
        boolean update = paramConfigService.lambdaUpdate()
                .set(ParamConfig::getStatus, "100")
                .eq(ParamConfig::getId, 101)
                .and(wrapper ->
                        wrapper.notIn(ParamConfig::getStatus, Lists.newArrayList("100", "-100"))
                                .or().isNull(ParamConfig::getStatus)
                ).update();
        System.out.println(update);
    }
}
