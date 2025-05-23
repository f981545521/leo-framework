package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.IPUtil;
import cn.acyou.leo.tool.dto.req.TaskSo;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.entity.ScheduleJobLog;
import cn.acyou.leo.tool.mapper.ScheduleJobLogMapper;
import cn.acyou.leo.tool.service.ScheduleJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 定时任务日志 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {

    @Override
    public void success(ScheduleJob scheduleJob, String remark, String logs, Integer times) {
        ScheduleJobLog jobLog = new ScheduleJobLog();
        jobLog.setJobId(scheduleJob.getJobId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setCreateTime(new Date());
        jobLog.setLocalIp(IPUtil.getLocalIP());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setStatus(Constant.CONS_1);
        jobLog.setTimes(times);
        jobLog.setRemark(remark);
        jobLog.setLogs(logs);
        save(jobLog);
    }

    /**
     * 错误
     *
     * @param scheduleJob  任务
     * @param errorMessage 错误消息
     */
    @Override
    public void error(ScheduleJob scheduleJob, String errorMessage) {
        ScheduleJobLog jobLog = new ScheduleJobLog();
        jobLog.setJobId(scheduleJob.getJobId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setCreateTime(new Date());
        jobLog.setLocalIp(IPUtil.getLocalIP());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setStatus(Constant.CONS_0);
        jobLog.setError(errorMessage);
        jobLog.setTimes(0);
        save(jobLog);
    }

    @Override
    public PageData<ScheduleJobLog> page(TaskSo taskSo) {
        return PageQuery.startPage(taskSo)
                .selectMapper(lambdaQuery()
                        .eq(taskSo.getJobId() != null, ScheduleJobLog::getJobId, taskSo.getJobId())
                        .eq(taskSo.getStatus() != null, ScheduleJobLog::getStatus, taskSo.getStatus())
                        .list());
    }
}
