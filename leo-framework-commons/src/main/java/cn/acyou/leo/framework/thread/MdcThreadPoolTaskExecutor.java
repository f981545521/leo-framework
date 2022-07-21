package cn.acyou.leo.framework.thread;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 带有任务追踪的线程池
 *
 * @author youfang
 * @version [1.0.0, 2022/7/21 9:55]
 **/
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(@NonNull Runnable command) {
        super.execute(MdcHelper.wrap(command, MdcHelper.getContextForTask()));
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return super.submit(MdcHelper.wrap(task, MdcHelper.getContextForTask()));
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit(MdcHelper.wrap(task, MdcHelper.getContextForTask()));
    }

}
