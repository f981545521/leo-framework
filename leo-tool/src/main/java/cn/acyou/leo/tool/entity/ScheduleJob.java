package cn.acyou.leo.tool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_schedule_job")
@ApiModel(value = "ScheduleJob对象", description = "定时任务")
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    @ApiModelProperty(value = "spring bean名称")
    private String beanName;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "任务状态  0：暂停  1：正常")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
