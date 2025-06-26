package cn.acyou.leo.tool.util;

import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.function.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author youfang
 * @version [1.0.0, 2025/6/23 15:21]
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonUtils {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedisTemplate(RedissonClient redissonClient) {
        RedissonUtils.redissonClient = redissonClient;
    }

    public static void lock(String lockKey, long waitTime, long leaseTime, Task task) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                task.run();
            }
        } catch (InterruptedException e) {
            log.error("RedissonUtils 获取锁中断", e);
            throw new ServiceException("RedissonUtils 获取锁中断", e);
        } finally {
            lock.unlock();
        }
    }

    public void lockKey(String s){
        redissonClient.getLock(s);
    }
}
