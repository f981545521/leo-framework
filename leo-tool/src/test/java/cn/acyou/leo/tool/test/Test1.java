package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.mapper.ParamConfigMapper;
import cn.acyou.leo.tool.retrofit.LeoPayApi;
import cn.acyou.leo.tool.service.ParamConfigService;
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
        Result<Object> aac = leoPayApi.printRequest(1000L, new HashMap<>(), "aac");
        System.out.println(aac);
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
}
