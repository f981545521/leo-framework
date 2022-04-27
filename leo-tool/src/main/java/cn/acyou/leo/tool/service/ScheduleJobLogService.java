package cn.acyou.leo.tool.service;

import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.entity.ScheduleJobLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 定时任务日志 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLog> {
    /**
     * 记录成功日志
     *
     * @param scheduleJob 任务
     * @param remark      备注
     * @param times       耗时
     */
    void success(ScheduleJob scheduleJob, String remark, Integer times);

    /**
     * 记录失败日志
     *
     * @param scheduleJob  任务
     * @param errorMessage 错误消息
     */
    void error(ScheduleJob scheduleJob, String errorMessage);
}
