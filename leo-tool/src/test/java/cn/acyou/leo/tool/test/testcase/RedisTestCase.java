package cn.acyou.leo.tool.test.testcase;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.test.ApplicationBaseTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/23 9:43]
 **/
@Slf4j
public class RedisTestCase extends ApplicationBaseTests {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void test1(){
        LoginUser loginUser = new LoginUser();
        loginUser.setToken("5");
        loginUser.setUserName("111");
        redisUtils.setObject("sys:token:5", loginUser);
    }

    @Test
    public void test2(){
        LoginUser user = redisUtils.getObject("sys:token:5");
        System.out.println(user);
    }
    @Test
    public void test3(){
        int count = 5;
        int time = 5;
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
        System.out.println(redisUtils.accessLimit("book", count, time));
    }
}
