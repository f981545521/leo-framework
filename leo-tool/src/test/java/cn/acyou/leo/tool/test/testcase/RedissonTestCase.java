package cn.acyou.leo.tool.test.testcase;

import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.framework.util.function.Task;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.test.ApplicationBaseTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 14:14]
 **/
@Slf4j
public class RedissonTestCase extends ApplicationBaseTests {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisUtils redisUtils;
    private final static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static int stock = 101;

    public void outStock1(){
        WorkUtil.trySleep(500);
        stock = stock - 5;
    }

    public synchronized void outStock2(){
        WorkUtil.trySleep(500);
        stock = stock - 5;
    }

    public void outStock3(){
        RLock lock = redissonClient.getLock("activity:100");
        try {
            lock.lock();
            WorkUtil.trySleep(500);
            stock = stock - 5;
        }finally {
            lock.unlock();
        }
    }

    public void outStock4(){
        RLock lock = redissonClient.getLock("activity:100");
        if (lock.tryLock()) {
            try {
                WorkUtil.trySleep(500);
                stock = stock - 5;
            }finally {
                lock.unlock();
            }
        }
    }

    public void outStock5(){
        redisUtils.doWork("activity:100", ()->{
            WorkUtil.trySleep(500);
            stock = stock - 5;
        });
    }
    public void outStock6(){
        redisUtils.doWork("activity:100", 60*1000, 60*1000, ()->{
            WorkUtil.trySleep(500);
            stock = stock - 5;
        });
    }

    @Test
    public void test1(){
        execTask(this::outStock1);
    }

    @Test
    public void test2(){
        execTask(this::outStock2);
    }

    @Test
    public void test3(){
        execTask(this::outStock3);
    }

    @Test
    public void test4(){
        execTask(this::outStock4);
    }

    @Test
    public void test5(){
        execTask(this::outStock5);
    }

    @Test
    public void test6(){
        execTask(this::outStock6);
    }

    public void execTask(Task task) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tasks.add(CompletableFuture.runAsync(() -> {
                log.info("线程：{}准备就绪", Thread.currentThread().getId());
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
                log.info("线程：{}执行完成", Thread.currentThread().getId());
            }, executorService));
        }
        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
        countDownLatch.countDown();
        voidCompletableFuture1.whenComplete((k,l)->{
            log.info("所有任务执行结束！stock数为:{}", stock);
        });
        voidCompletableFuture1.join();
        log.info("===MAIN END=== stock数为:{}", stock);
    }
}
