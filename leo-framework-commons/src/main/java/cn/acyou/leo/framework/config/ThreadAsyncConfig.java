package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.util.WorkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * spring通过 TaskExecutor来实现多线程并发编程。使用ThreadPoolExecutor可实现基于线程池的TaskExecutor
 *
 * <p>使用@EnableAsync开启对异步任务的支持，并通过在实际执行bean方法中使用@Async注解来声明一个异步任务</p>
 *
 * @author youfang
 * @version [1.0.0, 2022/5/16 11:36]
 **/
@Slf4j
@EnableAsync
@AutoConfigureAfter(ThreadPoolConfig.class)
@ConditionalOnBean(name = {"threadPoolTaskExecutor"})
public class ThreadAsyncConfig extends AsyncConfigurerSupport {

    @Autowired(required = false)
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return threadPoolExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable arg0, Method arg1, Object... arg2) -> {
            log.error("An error has occurred on async thread ========" + arg0.getMessage() + "========", arg0);
            WorkUtil.printFirstStack(arg0);
        };
    }
}
