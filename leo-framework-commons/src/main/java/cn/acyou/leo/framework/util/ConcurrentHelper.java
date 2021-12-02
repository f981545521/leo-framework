package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.exception.ConcurrentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地并发访问工具
 *
 * <pre>
 *    ConcurrentHelper.doWork("AAA", ()->{
 *       countTest = countTest-10;
 *   });
 * </pre>
 * @author fangyou
 * @version [1.0.0, 2021-11-02 17:01]
 */
@Slf4j
public class ConcurrentHelper {
    private static final ConcurrentHashMap<String, Long> cache = new ConcurrentHashMap<>();
    //标识 prefix
    private static final String LOCK_KEY_PREFIX = "LOCAL_LOCK:";
    //默认超时时间 60S
    private static final long DEFAULT_TIME_OUT = 60 * 1000;

    /**
     * 获得锁
     *
     * @param lockKey 锁定键
     * @return {@link String}
     */
    private synchronized static Long lock(String lockKey) {
        return lock(lockKey, DEFAULT_TIME_OUT);
    }

    /**
     * 获得锁
     *
     * @param lockKey     锁定键
     * @param milliSecond 毫秒
     * @return {@link String}
     */
    private synchronized static Long lock(String lockKey, long milliSecond) {
        Assert.notNull(lockKey, "lockKey 不能为空！");
        long now = System.currentTimeMillis();
        Long value = cache.get(LOCK_KEY_PREFIX + lockKey);
        Long lockId = now + milliSecond;
        if (value == null) {
            cache.put(LOCK_KEY_PREFIX + lockKey, lockId);
            return lockId;
        } else {
            if (value > now) {
                return null;
            } else {
                cache.put(LOCK_KEY_PREFIX + lockKey, lockId);
                return lockId;
            }
        }
    }

    /**
     * 解锁
     *
     * @param lockKey 锁Key
     * @param lockId  锁标识
     * @return boolean 是否成功
     */
    private synchronized static boolean unLock(String lockKey, Long lockId) {
        if (StringUtils.isEmpty(lockKey)) {
            return false;
        }
        if (lockId == null) {
            return false;
        }
        Long currentLockId = cache.get(LOCK_KEY_PREFIX + lockKey);
        if (currentLockId != null && currentLockId.equals(lockId)) {
            cache.remove(LOCK_KEY_PREFIX + lockKey);
            return true;
        }
        return false;
    }


    /**
     * 执行任务
     *
     * @param key  锁关键词
     * @param task 任务
     */
    public static void doWork(String key, Task task) {
        doWork(key, DEFAULT_TIME_OUT, task);
    }

    /**
     * 执行任务
     *
     * @param key  锁关键词
     * @param time 锁时间（毫秒）
     * @param task 任务
     */
    public static void doWork(String key, Long time, Task task) {
        Long lock = null;
        try {
            lock = ConcurrentHelper.lock(key, time);
            if (lock == null) {
                log.warn("Key:{} 正在处理中...", key);
                throw new ConcurrentException("正在处理中，请稍候...");
            }
            task.run();
        } finally {
            ConcurrentHelper.unLock(key, lock);
        }
    }

    public static <T> T doCallWork(String key, CallTask<T> callTask){
        Long lock = null;
        try {
            lock = ConcurrentHelper.lock(key);
            if (lock == null) {
                log.warn("Key:{} 正在处理中...", key);
                throw new ConcurrentException("正在处理中，请稍候...");
            }
            return callTask.run();
        } finally {
            ConcurrentHelper.unLock(key, lock);
        }
    }

    public interface Task {
        void run() throws RuntimeException;
    }

    public interface CallTask<T> {
        T run() throws RuntimeException;
    }
}
