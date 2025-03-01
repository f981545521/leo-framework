package cn.acyou.leo.tool.runner;

import cn.acyou.leo.framework.util.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/1]
 **/
@Component
public class AppCheckRunner implements ApplicationRunner {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void run(ApplicationArguments args) {
        //测试返回多个值
        //test1();

    }

    private void test1() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/member_growth.lua"));
        script.setResultType(List.class);
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202401", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202402", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202403", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202404", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202405", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202406", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202407", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202408", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202409", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202410", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202411", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202412", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202501", "10", "MEMBER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202502", "10", "MEMBER_GROWTH_KEY:1000");
    }
}
