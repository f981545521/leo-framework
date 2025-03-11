package cn.acyou.leo.tool.dto.req;

import cn.acyou.leo.framework.model.PageSo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2023/9/20 15:17]
 **/
@Data
public class TaskSo extends PageSo {

    @ApiModelProperty(value = "任务id")
    private Long jobId;

    @ApiModelProperty(value = "任务状态    0：失败    1：成功")
    private Integer status;

}
