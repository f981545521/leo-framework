package cn.acyou.leo.framework.thread;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * 带有任务追踪的计划线程池
 *
 * @author youfang
 * @version [1.0.0, 2022/7/21 9:59]
 **/
public class MdcThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {
    @NonNull
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Instant startTime) {
        return super.schedule(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Instant startTime, @NonNull Duration period) {
        return super.scheduleAtFixedRate(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime, period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Duration period) {
        return super.scheduleAtFixedRate(MdcHelper.wrap(task, MdcHelper.getContextForTask()), period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Instant startTime, @NonNull Duration delay) {
        return super.scheduleWithFixedDelay(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime, delay);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Duration delay) {
        return super.scheduleWithFixedDelay(MdcHelper.wrap(task, MdcHelper.getContextForTask()), delay);
    }

    @Override
    public void execute(@NonNull Runnable task) {
        super.execute(MdcHelper.wrap(task, MdcHelper.getContextForTask()));
    }

    @Override
    public void execute(@NonNull Runnable task, long startTimeout) {
        super.execute(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTimeout);
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

    @NonNull
    @Override
    public ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        return super.submitListenable(MdcHelper.wrap(task, MdcHelper.getContextForTask()));
    }

    @NonNull
    @Override
    public <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        return super.submitListenable(MdcHelper.wrap(task, MdcHelper.getContextForTask()));
    }

    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Trigger trigger) {
        return super.schedule(MdcHelper.wrap(task, MdcHelper.getContextForTask()), trigger);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Date startTime) {
        return super.schedule(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Date startTime, long period) {
        return super.scheduleAtFixedRate(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime, period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, long period) {
        return super.scheduleAtFixedRate(MdcHelper.wrap(task, MdcHelper.getContextForTask()), period);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Date startTime, long delay) {
        return super.scheduleWithFixedDelay(MdcHelper.wrap(task, MdcHelper.getContextForTask()), startTime, delay);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, long delay) {
        return super.scheduleWithFixedDelay(MdcHelper.wrap(task, MdcHelper.getContextForTask()), delay);
    }
}
