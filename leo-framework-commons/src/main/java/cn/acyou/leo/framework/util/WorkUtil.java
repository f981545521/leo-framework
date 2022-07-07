package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.util.function.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/31 14:55]
 **/
@Slf4j
public class WorkUtil {
    public static <T> T doCallWork(long timeout, TimeUnit unit, Supplier<T> supplier){
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);
        do {
            try {
                T t = supplier.get();
                if (t != null) {
                    return t;
                }
            } catch(IllegalThreadStateException ex) {
                if (rem > 0) {
                    try {
                        Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);
        return null;
    }

    public static void tryRun(Task task) {
        try {
            task.run();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //public static void main(String[] args) {
    //    tryRun(()->{
    //        System.out.println("ok");
    //        int i = 9/0;
    //        System.out.println("ok");
    //    });
    //    System.out.println("end");
    //}
}
