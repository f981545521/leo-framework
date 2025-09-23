package cn.acyou.leo.tool.util;

import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.function.Task;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.RedissonMultiLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

    public static void lock(List<String> lockKeys, long waitTime, long leaseTime, Task task) {
        RedissonMultiLock multiLock = new RedissonMultiLock(lockKeys.stream().map(k -> redissonClient.getLock(k)).toArray(RLock[]::new));
        try {
            long startTime = System.currentTimeMillis();
            log.info("RedissonMultiLock 准备获取锁 : {}", startTime);
            if (multiLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                log.info("RedissonMultiLock 获取到锁 耗时 : {}",  System.currentTimeMillis() - startTime);
                task.run();
            }
        } catch (InterruptedException e) {
            log.error("RedissonUtils 获取锁中断", e);
            throw new ServiceException("RedissonUtils 获取锁中断", e);
        } finally {
            multiLock.unlock();
        }
    }

    public void lockKey(String s){
        redissonClient.getLock(s);
    }


    /**
     * 尝试获取令牌（非阻塞）
     * @param key 限流key
     * @param rate 速率
     * @param rateInterval 时间间隔
     * @param rateIntervalUnit 时间单位
     * @param permits 需要的令牌数
     * @return 是否获取成功
     */
    public boolean tryAcquire(String key, long rate, long rateInterval,
                              RateIntervalUnit rateIntervalUnit, long permits) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        // 设置速率，如果已经设置过则不会重复设置
        rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, rateIntervalUnit);
        return rateLimiter.tryAcquire(permits);
    }

    /**
     * 获取令牌（阻塞）
     * @param key 限流key
     * @param rate 速率
     * @param rateInterval 时间间隔
     * @param rateIntervalUnit 时间单位
     * @param permits 需要的令牌数
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public boolean acquire(String key, long rate, long rateInterval,
                           RateIntervalUnit rateIntervalUnit, long permits,
                           long timeout, TimeUnit unit) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, rateIntervalUnit);
        return rateLimiter.tryAcquire(permits, timeout, unit);
    }

    /**
     * 获取剩余令牌数
     * @param key 限流key
     * @return 剩余令牌数
     */
    public long availablePermits(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        return rateLimiter.availablePermits();
    }

    /**
     * 删除限流器
     * @param key 限流key
     */
    public void delete(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.delete();
    }

    public RateLimiter createRateLimit(String key) {
        final RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        return new RateLimiter(rateLimiter);
    }

    @Data
    public class RateLimiter {
        private RRateLimiter rateLimiter;

        public RateLimiter(RRateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
        }

        public RateLimiter setRateAsync(long rate, long rateInterval){
            rateLimiter.setRateAsync(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.SECONDS)
                    .whenComplete((res, exception) -> {
                        if (exception != null) {
                            log.error("异步设置速率失败: " + exception.getMessage(), exception);
                        } else {
                            log.info("异步设置速率完成！[{}] {}/{}", rateLimiter.getName(), rate, rateInterval);
                        }
                    });
            return this;
        }

        public boolean tryAcquire(long permits, Long timeout) {
            if (timeout != null && timeout > 0) {
                return rateLimiter.tryAcquire(permits, timeout, TimeUnit.SECONDS);
            } else {
                return rateLimiter.tryAcquire(permits);
            }
        }

        public void acquire(long permits){
            rateLimiter.acquire(permits);

        }

        public long availablePermits(){
            return rateLimiter.availablePermits();
        }
    }

    public void shutdown() {
        if (redissonClient != null) {
            redissonClient.shutdown();
        }
    }




}
