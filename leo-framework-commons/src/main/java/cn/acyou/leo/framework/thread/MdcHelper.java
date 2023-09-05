package cn.acyou.leo.framework.thread;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/21 9:58]
 **/
class MdcHelper {

    /**
     * 获得任务上下文
     *
     * @return {@link Map}<{@link String}, {@link String}>
     */
    public static Map<String, String> getContextForTask() {
        return MDC.getCopyOfContextMap();
    }


    /**
     * 如果不存在设置追踪唯一id
     */
    private static void setTraceIdIfAbsent() {
        if (MDC.get("leoTraceNo") == null) {
            MDC.put("leoTraceNo", UUID.randomUUID().toString().replaceAll("-", ""));
        }
    }

    /**
     * 包装 Callable
     *
     * @param task    任务
     * @param context 上下文
     * @return {@link Callable}<{@link T}>
     */
    public static <T> Callable<T> wrap(final Callable<T> task, final Map<String, String> context) {
        return () -> {
            if (context != null && !context.isEmpty()) {
                MDC.setContextMap(context);
            }
            try {
                setTraceIdIfAbsent();
                return task.call();
            } finally {
                MDC.clear();
            }
        };
    }


    /**
     * 包装 Runnable
     *
     * @param runnable 可运行的
     * @param context  上下文
     * @return {@link Runnable}
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context != null && !context.isEmpty()) {
                MDC.setContextMap(context);
            }
            try {
                setTraceIdIfAbsent();
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
