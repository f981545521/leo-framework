package cn.acyou.leo.tool.service;

import cn.acyou.leo.tool.dto.task.ScheduleJobStatusVo;
import cn.acyou.leo.tool.entity.ScheduleJob;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 定时任务 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface ScheduleJobService extends IService<ScheduleJob> {

    /**
     * 运行任务
     *
     * @param jobId 任务ID
     */
    void run(Long jobId);

    /**
     * 暂停任务
     *
     * @param jobId 任务ID
     */
    void pause(Long jobId);

    /**
     * 任务状态
     *
     * @param jobId 任务ID
     */
    ScheduleJobStatusVo status(Long jobId);

    /**
     * 恢复任务
     *
     * @param jobId 任务ID
     */
    void resume(Long jobId);

    /**
     * 重新设置任务
     *
     * @param jobId 任务ID
     * @param cron  cron
     */
    void resetTime(Long jobId, String cron);
}
