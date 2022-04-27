package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.mapper.ScheduleJobMapper;
import cn.acyou.leo.tool.service.ScheduleJobService;
import cn.acyou.leo.tool.task.AbstractTaskParent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Override
    public void run(Long jobId) {
        ScheduleJob scheduleJob = getById(jobId);
        String beanName = scheduleJob.getBeanName();
        AbstractTaskParent iTask = SpringHelper.getBean(beanName);
        iTask.runJob(scheduleJob);
    }

    @Override
    public void pause(Long jobId) {
        ScheduleJob scheduleJob = getById(jobId);
        String beanName = scheduleJob.getBeanName();
        AbstractTaskParent iTask = SpringHelper.getBean(beanName);
        iTask.pauseJob(scheduleJob);
    }

    @Override
    public void resume(Long jobId) {
        ScheduleJob scheduleJob = getById(jobId);
        if (scheduleJob.getStatus().equals(0)) {
            scheduleJob.setStatus(1);
            updateById(scheduleJob);
        }
        String beanName = scheduleJob.getBeanName();
        AbstractTaskParent iTask = SpringHelper.getBean(beanName);
        iTask.resumeJob(scheduleJob);
    }

    @Override
    public void resetTime(Long jobId, String cron) {
        ScheduleJob scheduleJob = getById(jobId);
        scheduleJob.setCronExpression(cron);
        String beanName = scheduleJob.getBeanName();
        AbstractTaskParent iTask = SpringHelper.getBean(beanName);
        iTask.resetTime(scheduleJob);
    }
}
