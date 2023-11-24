package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.util.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * <h3>scheduleAtFixedRate 和 scheduleWithFixedDelay 方法之间的区别</h3>
 * <ul>
 *     <li>scheduleAtFixedRate 方法的执行是按照固定时间间隔进行执行的，我们可以理解为等差数列的执行方式，假设 n 为初始延迟，即 n 执行，n + period 执行，然后 n + 2 * period 执行，依次往后。但是必须等待上个任务执行完毕，下个任务才能开始执行，即实际上是这么执行的，假设 run 是任务执行时间，则 n 执行，然后 n + max(period,run) 依次执行。</li>
 *     <li>scheduleWithFixedDelay 方法的执行是上个任务执行完，然后过 delay 时间后执行下个任务，假设 n 为初始延迟，即 n 执行，n + run + delay 执行，然后 n + 2 * (run + delay) 执行。</li>
 * </ul>
 * <p>
 * 两种方法都是遇到异常后，后序都无法再执行。
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
    private static final ThreadPoolTaskScheduler scheduledExecutor;

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
    public static ThreadPoolTaskScheduler scheduleExecutor() {
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
        schedule(task, 5, TimeUnit.SECONDS);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task  任务
     * @param delay 延迟
     * @param unit  时间单位
     */
    public static void schedule(Runnable task, long delay, TimeUnit unit) {
        scheduledExecutor.schedule(task, new Date(unit.toMillis(delay) + System.currentTimeMillis()));
    }

    /**
     * 执行任务延迟执行
     *
     * @param task      任务
     * @param startTime 初始延迟
     * @param period    周期
     * @param unit      时间单位
     */
    public static void scheduleAtFixedRate(Runnable task, Date startTime, long period, TimeUnit unit) {
        scheduledExecutor.scheduleAtFixedRate(task, startTime, period);
    }

    /**
     * 执行任务延迟执行
     *
     * @param task         任务
     * @param startTime 初始延迟
     * @param delay        延迟
     */
    public static void scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        scheduledExecutor.scheduleWithFixedDelay(task, startTime, delay);
    }

    /**
     * 停止线程池
     */
    public static void shutdown() {
        taskExecutor.shutdown();
        scheduledExecutor.shutdown();
    }
}
