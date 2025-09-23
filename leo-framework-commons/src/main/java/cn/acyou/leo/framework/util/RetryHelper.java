package cn.acyou.leo.framework.util;


import cn.acyou.leo.framework.exception.RetryLaterException;

import java.util.Random;
import java.util.function.Supplier;

/**
 * @author youfang
 * @version [1.0.0, 2025/9/23 10:57]
 **/
public class RetryHelper {
    private final long initialInterval;
    private final long maxInterval;
    private final double multiplier;
    private final int maxAttempts;
    private final Random random = new Random();

    public RetryHelper(long initialInterval, long maxInterval, double multiplier, int maxAttempts) {
        this.initialInterval = initialInterval;
        this.maxInterval = maxInterval;
        this.multiplier = multiplier;
        this.maxAttempts = maxAttempts;
    }

    public <T> T execute(Supplier<T> task) throws Exception {
        long currentInterval = initialInterval;
        int attempt = 0;
        Exception lastException;
        do {
            attempt++;
            try {
                return task.get();
            } catch (Exception e) {
                lastException = e;
                if (attempt >= maxAttempts) {
                    break; // 达到最大重试次数
                }
                if (!(e instanceof RetryLaterException)) {
                    break;// 非重试异常
                }
                long waitTime = (long) (currentInterval/* * (0.9 + 0.2 * random.nextDouble())*/); // 计算下一次等待时间（加入随机抖动???）
                Thread.sleep(Math.min(waitTime, maxInterval));
                currentInterval = (long) (currentInterval * multiplier);// 指数增加间隔时间
            }
        } while (attempt < maxAttempts);
        throw lastException;
    }


}
