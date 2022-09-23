package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.util.function.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 工作工具
 *
 * @author youfang
 * @version [1.0.0, 2022/3/31 14:55]
 **/
@Slf4j
public class WorkUtil {

    /**
     * 执行有返回值的任务，返回null则重试，非空则结束
     *
     * @param timeout  超时
     * @param unit     单位
     * @param supplier 供应商
     * @return {@link T}
     */
    public static <T> T doCallWork(long timeout, TimeUnit unit, Supplier<T> supplier) {
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);
        do {
            try {
                T t = supplier.get();
                if (t != null) {
                    return t;
                }
            } catch (Exception ex) {
                if (rem > 0) {
                    try {
                        Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);
        return null;
    }


    /**
     * 执行忽略异常的任务
     *
     * @param task 任务
     */
    public static void tryRun(Task task) {
        try {
            task.run();
        } catch (Exception e) {
            printFirstStack(e);
        }
    }

    /**
     * 打印第一个栈信息
     *
     * @param e e
     */
    public static void printFirstStack(Throwable e) {
        String reason = e.getMessage();
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            StackTraceElement stackTraceElement = stackTrace[0];
            reason = reason + " ERROR MSG: " + stackTraceElement.getClassName() + "|" + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        }
        log.error(reason);
    }

    /**
     * 尝试阻塞线程 5s
     */
    public static void trySleep5000() {
        trySleep(5000);
    }

    /**
     * 尝试阻塞线程
     *
     * @param millis 毫秒
     */
    public static void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void watch(Task task) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        task.run();
        stopWatch.stop();
        log.info("StopWatch任务耗时：{}", stopWatch.getTotalTimeMillis());
    }
}
