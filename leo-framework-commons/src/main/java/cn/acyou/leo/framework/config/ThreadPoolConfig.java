package cn.acyou.leo.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * spring通过 TaskExecutor来实现多线程并发编程。使用ThreadPoolExecutor可实现基于线程池的TaskExecutor,
 * 使用@EnableAsync开启对异步任务的支持，并通过在实际执行bean方法中使用@Async注解来声明一个异步任务
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig extends AsyncConfigurerSupport {

    /**
     * 核心线程池大小
     */
    private static final int SCHEDULE_CORE_POOL_SIZE = 5;
    /**
     * 线程池名称
     */
    private static final String TASK_THREAD_NAME = "Leo-Executor-";
    /**
     * 计划线程池名称
     */
    private static final String SCHEDULED_THREAD_NAME = "Leo-Scheduled-Executor-%d";

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程名称的前缀
        executor.setThreadNamePrefix(TASK_THREAD_NAME);
        //核心线程池数量
        executor.setCorePoolSize(5);
        //最大线程数量
        executor.setMaxPoolSize(50);
        //线程池的队列容量
        executor.setQueueCapacity(100);
        //线程存活时间（秒）
        executor.setKeepAliveSeconds(300);
        //setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务 - CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(SCHEDULE_CORE_POOL_SIZE,
                new BasicThreadFactory.Builder().namingPattern(SCHEDULED_THREAD_NAME).daemon(true).build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
            }
        };
    }

    @Override
    public Executor getAsyncExecutor() {
        return threadPoolExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable arg0, Method arg1, Object... arg2) -> {
            log.error("==========================" + arg0.getMessage() + "=======================", arg0);
            log.error("exception method:" + arg1.getName());
        };
    }
}
