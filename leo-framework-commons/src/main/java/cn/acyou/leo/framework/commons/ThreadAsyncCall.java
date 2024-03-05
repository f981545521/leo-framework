package cn.acyou.leo.framework.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class ThreadAsyncCall {

    private static final DelayQueue<DelayTask> QUEUE = new DelayQueue<>();

    static {
        synchronized (ThreadAsyncCall.class) {
            log.info("thread async call 初始化");
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        if (QUEUE.size() != 0) {
                            DelayTask task = QUEUE.poll(10, TimeUnit.MINUTES);
                            try {
                                if (task != null) {
                                    Map<String, String> contextMap = task.getContextMap();
                                    MDC.setContextMap(contextMap);
                                    Supplier<Object> supplier = task.getSupplier();
                                    Object res = supplier.get();
                                    if (res == null) {
                                        //调用结果为空
                                        run(task.getSleep(), task.getSupplier());
                                    }
                                }
                            } catch (Throwable e) {
                                log.error("thread async call function run error.");
                                e.printStackTrace();
                            } finally {
                                MDC.clear();
                            }
                        } else {
                            Thread.sleep(500);
                        }
                    }
                } catch (Throwable e) {
                    log.error("thread async call system error.");
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.setName("ThreadAsyncCall");
            thread.start();
            log.info("thread async call 初始化完成");
        }
    }

    public static void run(Supplier<Object> supplier) {
        run(2000, supplier);
    }

    public static void run(long interval, Supplier<Object> supplier) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        DelayTask delayTask = new DelayTask(interval, supplier, contextMap, interval, TimeUnit.MILLISECONDS);
        QUEUE.add(delayTask);
    }


    @Data
    @AllArgsConstructor
    static class DelayTask implements Delayed {
        /**
         * 延迟执行的时间
         */
        private final long time;
        private long sleep;
        private Map<String, String> contextMap;
        private Supplier<Object> supplier;

        public DelayTask(long sleep, Supplier<Object> supplier, Map<String, String> contextMap, long time, TimeUnit unit) {
            this.sleep = sleep;
            this.supplier = supplier;
            this.contextMap = contextMap;
            this.time = System.currentTimeMillis() + (time > 0 ? unit.toMillis(time) : 0);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return time - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayTask Order = (DelayTask) o;
            long diff = this.time - Order.time;
            if (diff <= 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
