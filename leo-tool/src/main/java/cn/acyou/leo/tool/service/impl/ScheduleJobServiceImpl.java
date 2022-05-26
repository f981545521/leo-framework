package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.framework.util.UrlUtil;
import cn.acyou.leo.tool.dto.task.ScheduleJobStatusVo;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.mapper.ScheduleJobMapper;
import cn.acyou.leo.tool.service.ScheduleJobService;
import cn.acyou.leo.tool.task.base.AbstractTaskParent;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public ScheduleJobStatusVo status(Long jobId) {
        ScheduleJob scheduleJob = getById(jobId);
        String beanName = scheduleJob.getBeanName();
        AbstractTaskParent iTask = SpringHelper.getBean(beanName);
        String status = iTask.status();
        ScheduleJobStatusVo statusVo = new ScheduleJobStatusVo();
        statusVo.setStatus(status);
        if ("RUNNING".equals(status)) {
            String cronExpression = scheduleJob.getCronExpression();
            String encodeUrl = UrlUtil.encode(cronExpression.trim());
            Map<String, String> header = new HashMap<>();
            header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
            HttpResponse execute = HttpUtil.createPost("https://api.bejson.com/btools/othertools/cron/?crontxt=" + encodeUrl).headerMap(header, true).execute();
            String[] objs = JSON.parseObject(execute.body()).getString("obj").split("<br>");
            statusVo.setNextExecTimes(objs);
        }
        return statusVo;
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
