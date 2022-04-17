package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.util.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author youfang
 */
@Slf4j
public class AsyncManager {
    /**
     * 异步操作任务调度线程池
     */
    private static final ThreadPoolTaskExecutor taskExecutor;
    /**
     * 定时任务线程池
     */
    private static final ScheduledExecutorService scheduledExecutor;

    static {
        //使用的时候，没有Bean的时候会报错
        taskExecutor = SpringHelper.getBean("threadPoolTaskExecutor");
        scheduledExecutor = SpringHelper.getBean("scheduledExecutorService");
    }

    /**
     * 单例模式
     */
    private AsyncManager() {

    }


    /**
     * 获取线程池
     *
     * @return {@link ThreadPoolTaskExecutor}
     */
    public static ThreadPoolTaskExecutor executor(){
        return taskExecutor;
    }

    /**
     * 获取定时任务线程池
     *
     * @return {@link ScheduledExecutorService}
     */
    public static ScheduledExecutorService scheduleExecutor(){
        return scheduledExecutor;
    }


    /**
     * 执行任务 execute
     *
     * @param task 任务
     */
    public static void execute(Runnable task) {
        taskExecutor.execute(task);
    }

    /**
     * 提交
     * 执行任务 submit
     *
     * @param task 任务
     * @return 任务
     */
    public static Future<?> submit(Runnable task) {
        return taskExecutor.submit(task);
    }

    /**
     * 提交
     * 执行任务 submit
     *
     * @param <T> 类型
     * @param task 任务
     * @return 任务
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return taskExecutor.submit(task);
    }

    /**
     * 执行任务(默认延迟10ms)
     *
     * @param task 任务
     */
    public static void schedule(Runnable task) {
        //操作延迟5秒
        scheduledExecutor.schedule(task, 5, TimeUnit.SECONDS);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task  任务
     * @param delay 延迟
     * @param unit  时间单位
     */
    public static void schedule(Runnable task, long delay, TimeUnit unit) {
        scheduledExecutor.schedule(task, delay, unit);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task         任务
     * @param initialDelay 初始延迟
     * @param period       周期
     * @param unit         时间单位
     */
    public static void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task         任务
     * @param initialDelay 初始延迟
     * @param delay        延迟
     * @param unit         时间单位
     */
    public static void scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }

    /**
     * 停止线程池
     */
    public static void shutdown() {
        taskExecutor.shutdown();
        scheduledExecutor.shutdown();
    }
}
