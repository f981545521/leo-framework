package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.function.CallTask;
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
     * 尝试阻塞线程 1s
     */
    public static void trySleep1000() {
        trySleep(1000);
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

    /**
     * 重试工作
     *
     * @param times 次
     * @param task  任务
     * @return {@link T}
     */
    public static <T> T doRetryWork(int times, CallTask<T> task) {
        int currentTime = 0;
        while (currentTime < times) {
            try {
                return task.run();
            } catch (Exception e) {
                log.error("doRetryWorkError 执行任务出错", e);
                currentTime++;
                trySleep(2000);
            }
        }
        return null;
    }

    /**
     * 重试工作
     *
     * @param times 次
     * @param task  任务
     */
    public static void doRetryWork(int times, Task task) {
        int currentTime = 0;
        while (currentTime < times) {
            try {
                task.run();
                return;
            } catch (Exception e) {
                log.error("doRetryWorkError 执行任务出错", e);
                currentTime++;
                trySleep(2000);
            }
        }
    }

    /**
     * 重试工作
     *
     * @param times 次
     * @param task  任务
     */
    public static void doRetryWorkGradually(int times, Task task) {
        doRetryWorkGradually(1, times, task);
    }
    /**
     * 重试工作
     *
     * @param times 次
     * @param task  任务
     */
    private static void doRetryWorkGradually(int currentTimes, int times, Task task) {
        AsyncManager.schedule(()->{
            if (currentTimes <= times) {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("doRetryWorkGradually 执行任务出错", e);
                    doRetryWorkGradually(currentTimes + 1, times, task);
                }
            }
        }, getGraduallyRule(currentTimes), TimeUnit.SECONDS);
    }

    private static int getGraduallyRule(int currentTimes){
        if (currentTimes <= 1) {
            return 1;
        }
        if (currentTimes == 2) {
            return 15;
        }
        if (currentTimes == 3) {
            return 15;
        }
        if (currentTimes == 4) {
            return 30;
        }
        if (currentTimes == 5) {
            return 3*60;
        }
        if (currentTimes == 6) {
            return 10*60;
        }
        return 10*60;
    }

    public static void watch(Task task) {
        StopWatch stopWatch = new StopWatch();
        log.info("任务耗时监控 开始 -> ...");
        stopWatch.start();
        task.run();
        stopWatch.stop();
        log.info("任务耗时监控 结束 <- 耗时：{}ms", stopWatch.getTotalTimeMillis());
    }

    public static <T> T doWaitWork(long waitTimeout, long interval, CallTask<T> task) {
        final long deadline = System.currentTimeMillis() + waitTimeout;
        if (waitTimeout <= 0L) {
            return task.run();
        }
        try {
            do {
                T r = task.run();
                if (r != null) {
                    return r;
                }
                trySleep(Math.min(interval, deadline - System.currentTimeMillis()));
            } while (System.currentTimeMillis() < deadline);
        } catch (Exception e) {
            log.error("内部方法发生错误", e);
            return null;
        }
        throw new ServiceException(CommonErrorEnum.NO_WAIT_RESULT_ERROR);
    }

    /**
     * 吞并异常来执行 异常时返回NULL
     * @param callTask 有返回值
     * @return {@link T}
     */
    public static <T> T swallowException(CallTask<T> callTask) {
        try {
            return callTask.run();
        }catch (Exception e) {
            printFirstStack(e);
            return null;
        }
    }

    /**
     * 执行方法返回bool
     * @param supplier 先执行的方法
     * @return boolean
     */
    public static boolean runBool(Supplier<Boolean> supplier) {
        return supplier.get() != null ? supplier.get() : false;
    }

    /**
     * 执行方法返回true
     * @param task 先执行的方法
     * @return boolean
     */
    public static boolean runTrue(Task task) {
        task.run();
        return true;
    }

    /**
     * 执行方法返回false
     * @param task 先执行的方法
     * @return boolean
     */
    public static boolean runFalse(Task task) {
        task.run();
        return false;
    }
}
