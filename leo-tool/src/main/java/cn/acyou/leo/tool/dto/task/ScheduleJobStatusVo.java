package cn.acyou.leo.tool.dto.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author youfang
 * @date 2020/12/28 10:45
 */
@Data
public class ScheduleJobStatusVo {

    @ApiModelProperty("任务状态  RUNNING  STOP")
    private String status;

    @ApiModelProperty("下次执行时间")
    private String[] nextExecTimes;


}
