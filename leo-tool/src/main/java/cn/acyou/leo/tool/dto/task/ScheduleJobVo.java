package cn.acyou.leo.tool.dto.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author youfang
 * @date 2020/12/28 10:45
 */
@Data
public class ScheduleJobVo {

    @ApiModelProperty("任务id")
    private Long jobId;

    @ApiModelProperty("bean名称")
    private String beanName;

    @ApiModelProperty("参数")
    private String params;

    @ApiModelProperty("cron表达式")
    private String cronExpression;

    @ApiModelProperty("任务状态  0：正常  1：暂停")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

}
