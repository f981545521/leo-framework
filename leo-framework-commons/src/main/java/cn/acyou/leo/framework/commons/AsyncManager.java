package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.util.Assert;
import cn.acyou.leo.framework.util.SpringHelper;
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
public class AsyncManager {

    /**
     * 异步操作任务调度线程池
     */
    private final ThreadPoolTaskExecutor taskExecutor = SpringHelper.getBean("threadPoolTaskExecutor");

    /**
     * 定时任务线程池
     */
    private final ScheduledExecutorService scheduledExecutor = SpringHelper.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager() {

    }

    private static final AsyncManager ME = new AsyncManager();

    public static AsyncManager me() {
        return ME;
    }

    /**
     * 执行任务 execute
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        Assert.notNull(taskExecutor, "请先配置线程池：[threadPoolTaskExecutor]");
        taskExecutor.execute(task);
    }

    /**
     * 提交
     * 执行任务 submit
     *
     * @param task 任务
     * @return 任务
     */
    public Future<?> submit(Runnable task) {
        Assert.notNull(taskExecutor, "请先配置线程池：[threadPoolTaskExecutor]");
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
    public <T> Future<T> submit(Callable<T> task) {
        Assert.notNull(taskExecutor, "请先配置线程池：[threadPoolTaskExecutor]");
        return taskExecutor.submit(task);
    }

    /**
     * 执行任务(默认延迟10ms)
     *
     * @param task 任务
     */
    public void schedule(Runnable task) {
        Assert.notNull(scheduledExecutor, "请先配置线程池：[scheduledExecutorService]");
        //操作延迟10毫秒
        scheduledExecutor.schedule(task, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task  任务
     * @param delay 延迟
     * @param unit  时间单位
     */
    public void schedule(Runnable task, long delay, TimeUnit unit) {
        Assert.notNull(scheduledExecutor, "请先配置线程池：[scheduledExecutorService]");
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
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        Assert.notNull(scheduledExecutor, "请先配置线程池：[scheduledExecutorService]");
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
    public void scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        Assert.notNull(scheduledExecutor, "请先配置线程池：[scheduledExecutorService]");
        scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }

    /**
     * 停止线程池
     */
    public void shutdown() {
        taskExecutor.shutdown();
        scheduledExecutor.shutdown();
    }
}
