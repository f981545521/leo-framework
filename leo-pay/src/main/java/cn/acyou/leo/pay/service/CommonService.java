package cn.acyou.leo.pay.service;

import cn.acyou.leo.framework.advisor.RedisLock;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 11:06]
 **/
@Service
public class CommonService {
    @RedisLock(key = "#name")
    public String testRedisLock(String name) {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello" + name;
    }
}
