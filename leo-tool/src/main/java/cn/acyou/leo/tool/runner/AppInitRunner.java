package cn.acyou.leo.tool.runner;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.service.ParamConfigService;
import cn.acyou.leo.tool.service.ScheduleJobService;
import cn.acyou.leo.tool.task.base.AbstractTaskParent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用`@PostConstruct`注解实现的时候，发现未完全注入问题，List中第一个元素需要注入的属性都为null！！
 * 使用ApplicationRunner来解决此问题
 *
 * @author youfang
 * @version [1.0.0, 2020/7/1]
 **/
@Slf4j
@Component
public class AppInitRunner implements ApplicationRunner {
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private ParamConfigService paramConfigService;

    @Override
    public void run(ApplicationArguments args) {
        /* ———————————————— 定时任务 ———————————————— */
        List<ScheduleJob> scheduleJobs = scheduleJobService.lambdaQuery().eq(ScheduleJob::getStatus, Constant.FLAG_TRUE_1).list();
        for (ScheduleJob scheduleJob : scheduleJobs) {
            if (SpringHelper.containsBean(scheduleJob.getBeanName())) {
                AbstractTaskParent iTask = SpringHelper.getBean(scheduleJob.getBeanName());
                iTask.resumeJob(scheduleJob);
            } else {
                log.error("定时器 {} 不存在，请检查", scheduleJob.getBeanName());
            }
        }
        //在这里修改默认的返回code与返回信息
        //Result.setDefaultSuccess(0, "SUCCESS");
        //Result.setDefaultError(-1, "ERROR");
    }
}
