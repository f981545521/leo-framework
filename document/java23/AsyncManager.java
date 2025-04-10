package com.hxl.member.common;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author youfang
 * @version [1.0.0, 2025/4/10 11:42]
 **/
public class AsyncManager {

    public static void execute(Runnable runnable) {
        CompletableFuture.runAsync(runnable, Executors.newVirtualThreadPerTaskExecutor());
    }

    static final AtomicInteger atomicInteger = new AtomicInteger();
    static final CountDownLatch latch = new CountDownLatch(10_000);
    static final Runnable runnable = () -> {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch(Exception e) {
            System.out.println(e);
        }
        System.out.println("Work Done - " + atomicInteger.incrementAndGet());
        latch.countDown();
    };

    public static void main1(String[] args) {
        Instant start = Instant.now();
        try (var executor = Executors.newFixedThreadPool(100)) {
            for(int i = 0; i < 10_000; i++) {
                executor.submit(runnable);
            }
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("总耗时 : " + timeElapsed);
    }


    public static void main(String[] args) {
        Instant start = Instant.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for(int i = 0; i < 10_000; i++) {
                executor.submit(runnable);
            }
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("总耗时 : " + timeElapsed);
    }


    public static void main4(String[] args) throws InterruptedException {
        Instant start = Instant.now();
        for(int i = 0; i < 10_000; i++) {
            //Executors.newVirtualThreadPerTaskExecutor().submit(runnable);//总耗时 : 2081
            //Thread.startVirtualThread(runnable);//总耗时 : 1946
            CompletableFuture.runAsync(runnable, Executors.newVirtualThreadPerTaskExecutor());
        }
        latch.await();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("总耗时 : " + timeElapsed);
    }
}
