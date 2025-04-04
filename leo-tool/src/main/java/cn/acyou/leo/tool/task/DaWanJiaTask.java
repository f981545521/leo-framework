package cn.acyou.leo.tool.task;

import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.HttpUtil2;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.task.base.AbstractTaskParent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/27 14:19]
 **/
@Slf4j
@Component
public class DaWanJiaTask extends AbstractTaskParent {
    @Autowired
    private RedisUtils redisUtils;
    private static final String PREFIX = "DAWANJIA:";

    @Override
    public void run(String params) {
        String[] split = params.split(",");
        for (int i = 0; i < split.length; i++) {
            exec(split[i]);
        }
    }

    public void exec(String token) {
        log.info("执行了DaWanJiaTask...");
        Map<String, String> header = new HashMap<>();
        header.put("authorization", token);
        String s = HttpUtil2.get("https://pw.gzych.vip/ykb_huiyuan/api/v1/MemberMine/BasicInfo", new HashMap<>(), header);
        addLog(s);
        Date now = new Date();
        if (DateUtil.afterTime(now, "07:01:00")) {
            String key = PREFIX + DateUtil.getDateShortFormat(now) + token;
            Boolean hasKey = redisUtils.hasKey(key);
            addLog(String.format("当前是否签到：%s", hasKey));
            if (!hasKey) {
                String s2 = HttpUtil2.get("https://pw.gzych.vip/ykb_huiyuan/api/v1/MemberCheckIn/Submit", new HashMap<>(), header);
                addLog(String.format("执行签到：%s", s2));
                redisUtils.set(key, "1", 2, TimeUnit.DAYS);
            }
        }
    }
}
