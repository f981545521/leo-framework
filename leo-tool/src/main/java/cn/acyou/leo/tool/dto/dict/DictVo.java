package cn.acyou.leo.tool.dto.dict;

import cn.acyou.leo.framework.model.Internationalized;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/16]
 **/
@Data
public class DictVo extends Internationalized {
    private Long id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典码
     */
    private String code;
    /**
     * 父节点ID 如果是0代表是顶级字典
     */
    private Long parentId;
    /**
     * 排序值
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态:  1-正常
     */
    private Integer status;

    @ApiModelProperty(value = "创建时间，默认当前创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建时间，默认当前创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
