package cn.acyou.leo.pay.service;

import cn.acyou.leo.framework.advisor.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 11:06]
 **/
@Slf4j
@Service
public class CommonService {
    @RedisLock(key = "#name", waitTime = 5000)
    public String testRedisLock(String name) {
        try {
            Thread.sleep(8000L);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return "hello" + name;
    }
}
