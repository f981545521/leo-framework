package cn.acyou.leo.tool.runner;

import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.hutool.core.io.IoUtil;
import com.google.common.collect.Lists;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/1]
 **/
@Component
public class AppCheckRunner implements ApplicationRunner {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void run(ApplicationArguments args) {
        //test1();
        //test2();
        //test3();
        //test4();
        test5();
    }

    private void test7() throws Exception {
        RScript script2 = redissonClient.getScript(StringCodec.INSTANCE);
        ClassPathResource resource = new ClassPathResource("lua/member_growth.lua");
        String read = IoUtil.read(resource.getInputStream(), StandardCharsets.UTF_8);
        String memberGrowthLuaSha = script2.scriptLoad(read);
        Object o = script2.evalSha(RScript.Mode.READ_WRITE, memberGrowthLuaSha, RScript.ReturnType.VALUE, Lists.newArrayList(), "202403", "10", "USER_GROWTH_KEY:1000");
        System.out.println(o);
    }

    private void test6() throws Exception {
        ClassPathResource resource = new ClassPathResource("lua/member_growth.lua");
        String luaScript = IoUtil.read(resource.getInputStream(), StandardCharsets.UTF_8);
        Long v = redissonClient.getScript().eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.VALUE,
                Lists.newArrayList(), "202403", "10", "USER_GROWTH_KEY:1000"
        );
        System.out.println(v);
    }

    private void test5() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/sale.lua"));
        script.setResultType(List.class);
        //黑名单
        redisUtils.getRedisTemplate().opsForSet().add("LEO:BLACK_LIST", "100");
        //抢购商品ID为10001，库存数量为5
        redisUtils.getRedisTemplate().opsForValue().set("LEO:PRODUCT:10001", "5");
        //用户抢购记录
        redisUtils.getRedisTemplate().delete("LEO:USER_RECORD");
        //开始
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD"), "100", "1", "10"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "100", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1000", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1000", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1001", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1002", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1003", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1004", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1005", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1006", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1007", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1008", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "1009", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10010", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10011", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10012", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10013", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10014", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10015", "1", "10", "1"));
        System.out.println(redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:PRODUCT:10001", "LEO:USER_RECORD", "LEO:BLACK_LIST"), "10016", "1", "10", "1"));
    }


    private void test4(){
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/rate_limit.lua"));
        script.setResultType(String.class);
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), "30","1", System.currentTimeMillis() + "");
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), "30","1", System.currentTimeMillis() + "");
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), "30","1", System.currentTimeMillis() + "");
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), "30","1", System.currentTimeMillis() + "");
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), "30","1", System.currentTimeMillis() + "");
    }

    private void test3(){
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/rate_limit.lua"));
        script.setResultType(String.class);
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), 1000, 50);
        redisUtils.getRedisTemplate().execute(script, Lists.newArrayList("LEO:ACCESS_LIMIT:100"), 1000, 50);
    }

    private void test2(){
        //直接执行脚本返回
        redisUtils.getRedisTemplate().execute(new DefaultRedisScript<>("return {1, 'ok'}", List.class), new ArrayList<>());

        redisUtils.getRedisTemplate().execute(new DefaultRedisScript<>(
                "local v = tonumber(ARGV[1])\r\n" +
                "return v", String.class), new ArrayList<>(), "202503");
    }

    private void test1() {
        //测试返回多个值
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/member_growth.lua"));
        script.setResultType(List.class);
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202401", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202402", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202403", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202404", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202405", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202406", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202407", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202408", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202409", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202410", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202411", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202412", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202501", "10", "USER_GROWTH_KEY:1000");
        redisUtils.getRedisTemplate().execute(script, new ArrayList<>(), "202502", "10", "USER_GROWTH_KEY:1000");
    }
}
