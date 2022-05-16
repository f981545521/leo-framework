package cn.acyou.leo.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Leo-Framework默认线程池配置
 * <br/>
 * 注意：异步支持{@link ThreadAsyncConfig}中使用到线程池。
 * 若想调整线程池参数，自定义Bean覆盖即可。
 * <br/>
 * 参照：
 * <code>@Bean(name = "threadPoolTaskExecutor")</code>
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程池大小
     */
    private static final int SCHEDULE_CORE_POOL_SIZE = 5;
    /**
     * 线程池名称
     */
    private static final String TASK_THREAD_NAME = "Leo-executor-";
    /**
     * 计划线程池名称
     */
    private static final String SCHEDULED_THREAD_NAME = "Leo-scheduled-%d";

    @ConditionalOnMissingBean(name = "threadPoolTaskExecutor")
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
    @ConditionalOnMissingBean(name = "scheduledExecutorService")
    @Bean(name = "scheduledExecutorService")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(SCHEDULE_CORE_POOL_SIZE);
        scheduler.setThreadFactory(new BasicThreadFactory.Builder().namingPattern(SCHEDULED_THREAD_NAME).daemon(true).build());
        return scheduler;
    }

}
