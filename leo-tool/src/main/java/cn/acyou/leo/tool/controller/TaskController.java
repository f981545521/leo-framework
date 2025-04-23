package cn.acyou.leo.tool.controller;


import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.tool.dto.req.TaskSo;
import cn.acyou.leo.tool.dto.task.ScheduleJobStatusVo;
import cn.acyou.leo.tool.dto.task.ScheduleJobVo;
import cn.acyou.leo.tool.dto.task.TaskVo;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.entity.ScheduleJobLog;
import cn.acyou.leo.tool.service.ScheduleJobLogService;
import cn.acyou.leo.tool.service.ScheduleJobService;
import cn.acyou.leo.tool.task.base.AbstractTaskParent;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-4 下午 09:30]
 **/
@RestController
@Api(tags = "定时任务")
@RequestMapping(value = "task")
public class TaskController {

    @Autowired(required = false)
    private List<AbstractTaskParent> iTaskList = Lists.newArrayList();

    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;


    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存/修改 定时器")
    public Result<Void> saveOrUpdate(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.saveOrUpdate(scheduleJob);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取所有定时任务")
    public Result<List<ScheduleJobVo>> list() {
        return Result.success(BeanCopyUtil.copyList(scheduleJobService.list(), ScheduleJobVo.class));
    }

    @PostMapping("/tasks")
    @ApiOperation(value = "获取所有定时器")
    public Result<List<TaskVo>> tasks() {
        List<TaskVo> taskVoList = new ArrayList<>();
        for (AbstractTaskParent task : iTaskList) {
            TaskVo taskVo = new TaskVo();
            taskVo.setName(task.getClass().getName());
            taskVo.setSimpleName(Introspector.decapitalize(task.getClass().getSimpleName()));
            taskVoList.add(taskVo);
        }
        return Result.success(taskVoList);
    }

    @PostMapping("/run/{jobId}")
    @ApiOperation(value = "立即执行任务", notes = "根据jobId手动执行任务")
    public Result<String> run(@PathVariable("jobId") Long jobId) {
        scheduleJobService.run(jobId);
        return Result.success();
    }

    @ApiOperation(value = "暂停定时任务")
    @PostMapping("/pause/{jobId}")
    public Result<String> pause(@PathVariable("jobId") Long jobId) {
        scheduleJobService.pause(jobId);
        return Result.success();
    }

    @ApiOperation(value = "查看定时任务状态")
    @PostMapping("/status/{jobId}")
    public Result<ScheduleJobStatusVo> status(@PathVariable("jobId") Long jobId) {
        ScheduleJobStatusVo statusVo = scheduleJobService.status(jobId);
        return Result.success(statusVo);
    }

    @PostMapping("/resume/{jobId}")
    @ApiOperation(value = "恢复定时任务")
    public Result<String> resume(@PathVariable("jobId") Long jobId) {
        scheduleJobService.resume(jobId);
        return Result.success();
    }

    @PostMapping("/delete/{jobId}")
    @ApiOperation(value = "删除定时任务")
    public Result<String> delete(@PathVariable("jobId") Long jobId) {
        scheduleJobService.removeById(jobId);
        return Result.success();
    }

    @PostMapping("/logs")
    @ApiOperation(value = "获取定时任务执行日志")
    public Result<PageData<ScheduleJobLog>> logs(@RequestBody TaskSo taskSo) {
        PageData<ScheduleJobLog> scheduleJobLogPageData = scheduleJobLogService.page(taskSo);
        return Result.success(scheduleJobLogPageData);
    }

    @PostMapping("/logs/export")
    @ApiOperation(value = "获取定时任务执行日志")
    public void logsExport(@RequestBody TaskSo taskSo, HttpServletResponse response) throws Exception {
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).head(head()).build();
        excelWriter.finish();
    }

    @PostMapping("/resetTime")
    @ApiOperation(value = "重置定时任务执行时间")
    public Result<Void> resetTime(Long jobId, String cron) {
        scheduleJobService.resetTime(jobId, cron);
        return Result.success();
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }
}
