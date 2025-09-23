package cn.acyou.leo.tool.test.testcase;

import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.tool.test.ApplicationBaseTests;
import cn.acyou.leo.tool.util.RedissonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RateIntervalUnit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author youfang
 * @version [1.0.0, 2025/9/22]
 **/
@Slf4j
public class RateLimiterExample extends ApplicationBaseTests {

    @Test
    public void  test111() throws InterruptedException {
        // 创建限流工具实例
        final RedissonUtils rateLimiterUtil = SpringHelper.getBean(RedissonUtils.class);
        // 示例1：简单限流
        simpleRateLimit(rateLimiterUtil);

        // 示例2：并发测试
        concurrentRateLimit(rateLimiterUtil);

        rateLimiterUtil.shutdown();
    }

    /**
     * 简单限流示例
     */
    private static void simpleRateLimit(RedissonUtils rateLimiterUtil) {
        String key = "api:user:query";

        // 设置限流：每秒最多2个请求
        for (int i = 1; i <= 5; i++) {
            boolean acquired = rateLimiterUtil.acquire(key, 2, 1, RateIntervalUnit.SECONDS, 1, 60, TimeUnit.SECONDS);
            if (acquired) {
                System.out.println("第" + i + "次请求: 成功");
            } else {
                System.out.println("第" + i + "次请求: 被限流");
            }

            // 模拟请求间隔
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        rateLimiterUtil.delete(key); // 清理
        System.out.println("简单限流示例结束\n");
    }

    /**
     * 并发限流示例
     */
    private static void concurrentRateLimit(RedissonUtils rateLimiterUtil) throws InterruptedException {
        String key = "api:order:create";
        int threadCount = 10;
        int requestPerThread = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger limitCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount * requestPerThread);

        // 设置限流：每分钟最多30个请求
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < requestPerThread; j++) {
                    boolean acquired = rateLimiterUtil.tryAcquire(key, 30, 1, RateIntervalUnit.MINUTES, 1);

                    if (acquired) {
                        successCount.incrementAndGet();
                        System.out.println("线程" + threadId + "-请求" + j + ": 成功");
                    } else {
                        limitCount.incrementAndGet();
                        System.out.println("线程" + threadId + "-请求" + j + ": 被限流");
                    }

                    latch.countDown();

                    // 随机间隔
                    try {
                        Thread.sleep((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();

        System.out.println("\n并发测试结果:");
        System.out.println("总请求数: " + (threadCount * requestPerThread));
        System.out.println("成功数: " + successCount.get());
        System.out.println("被限流数: " + limitCount.get());
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        System.out.println("剩余令牌: " + rateLimiterUtil.availablePermits(key));

        executor.shutdown();
        rateLimiterUtil.delete(key); // 清理
    }

    /**
     * 并发限流示例
     */
    private static void concurrentRateLimitV21(RedissonUtils rateLimiterUtil) throws InterruptedException {
        String key = "api:order:create";
        int threadCount = 10;
        int requestPerThread = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger limitCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount * requestPerThread);

        // 设置限流：每分钟最多30个请求
        long startTime = System.currentTimeMillis();

        final RedissonUtils.RateLimiter rateLimit = rateLimiterUtil.createRateLimit(key);
        rateLimit.setRateAsync(30, 1);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < requestPerThread; j++) {
                    rateLimit.tryAcquire(30, 1L, ()->{
                        System.out.println(1);
                        return "ok";
                    });
                    boolean acquired = rateLimiterUtil.tryAcquire(key, 30, 1, RateIntervalUnit.MINUTES, 1);

                    if (acquired) {
                        successCount.incrementAndGet();
                        System.out.println("线程" + threadId + "-请求" + j + ": 成功");
                    } else {
                        limitCount.incrementAndGet();
                        System.out.println("线程" + threadId + "-请求" + j + ": 被限流");
                    }

                    latch.countDown();

                    // 随机间隔
                    try {
                        Thread.sleep((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();

        System.out.println("\n并发测试结果:");
        System.out.println("总请求数: " + (threadCount * requestPerThread));
        System.out.println("成功数: " + successCount.get());
        System.out.println("被限流数: " + limitCount.get());
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        System.out.println("剩余令牌: " + rateLimiterUtil.availablePermits(key));

        executor.shutdown();
        rateLimiterUtil.delete(key); // 清理
    }
}
