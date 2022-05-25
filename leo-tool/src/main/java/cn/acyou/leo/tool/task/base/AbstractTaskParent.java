package cn.acyou.leo.tool.task.base;


import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.service.ScheduleJobLogService;
import cn.acyou.leo.tool.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务父类
 * <p>
 * 因为子类要实例化前，一定会先实例化他的父类。这样创建了继承抽象类的子类的对象，也就具有其父类（抽象类）所有属性和方法了
 * <p>
 * 继承此类，重写抽象方法{@link #run(String)}
 *
 * @author youfang
 * @version [1.0.0, 2020-4-4 下午 11:26]
 **/
@Slf4j
public abstract class AbstractTaskParent {

    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 任务并发处理
     */
    private static final String TASK_RUNNING_LOCK = "TASK:RUNNING:LOCK:";

    private ScheduleJob scheduleJob;
    private ScheduledFuture<?> future;
    private TriggerContext triggerContext = new SimpleTriggerContext();
    private List<String> logs = new ArrayList<>();

    /**
     * 抽象方法 ： 子类必须重写
     *
     * @param params 参数
     */
    public abstract void run(String params);

    /**
     * 记录日志并执行任务！
     *
     * @param auto 是否为自动
     */
    private void recordLogStart(boolean auto) {
        String lockId = redisUtils.lock(TASK_RUNNING_LOCK + scheduleJob.getJobId());
        if (lockId != null) {
            try {
                long start = System.currentTimeMillis();
                run(scheduleJob.getParams());
                long times = System.currentTimeMillis() - start;
                String remarkLog = String.join("\r\n", logs);
                if (auto) {
                    scheduleJobLogService.success(scheduleJob, "自动执行成功", remarkLog, (int) times);
                } else {
                    scheduleJobLogService.success(scheduleJob, "手动执行成功", remarkLog, (int) times);
                }
            } catch (Exception e) {
                e.printStackTrace();
                scheduleJobLogService.error(scheduleJob, e.getMessage());
            }
            redisUtils.unLock(TASK_RUNNING_LOCK + scheduleJob.getJobId(), lockId);
        } else {
            log.warn("已经有任务正在执行...");
        }
    }

    /**
     * 添加日志
     *
     * @param log 日志
     */
    protected void addLog(String log) {
        logs.add(log);
    }


    /**
     * 执行定时任务接口
     *
     * @param job 任务
     */
    public void runJob(ScheduleJob job) {
        scheduleJob = job;
        recordLogStart(false);
        log.info("run job method : " + job.getBeanName());
    }

    /**
     * 停止定时任务接口
     *
     * @param job 任务
     */
    public void pauseJob(ScheduleJob job) {
        if (future != null) {
            future.cancel(true);
        }
        job.setStatus(Constant.FLAG_FALSE_0);
        scheduleJobService.updateById(job);
        log.info("stop job : " + job.getBeanName());
    }

    /**
     * 恢复定时任务接口
     *
     * @param job 任务
     */
    public void resumeJob(ScheduleJob job) {
        if (future != null) {
            future.cancel(true);
        }
        scheduleJob = job;
        final CronTrigger cronTrigger = new CronTrigger(job.getCronExpression());
        future = threadPoolTaskScheduler.schedule(() -> recordLogStart(true), cronTrigger);
        log.info("resume job start : {} | next exec time: {}", job.getBeanName(), DateUtil.getDateFormat(cronTrigger.nextExecutionTime(triggerContext)));
    }

    /**
     * 重新设置定时任务执行时间
     *
     * @param job 任务
     */
    public void resetTime(ScheduleJob job) {
        //先停止
        if (future != null) {
            future.cancel(true);
        }
        job.setStatus(Constant.FLAG_FALSE_0);
        scheduleJobService.updateById(job);
        //再启动
        future = threadPoolTaskScheduler.schedule(() -> recordLogStart(true), new CronTrigger(job.getCronExpression()));
        log.info("resetTime job : " + job.getBeanName());
    }
}
