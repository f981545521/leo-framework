package cn.acyou.leo.tool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author youfang
 * @since 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_param_config")
@ApiModel(value="ParamConfig对象", description="参数配置表")
public class ParamConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数主键(PK)")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "命名空间/组")
    private String namespace;

    @ApiModelProperty(value = "参数编码(键名)")
    private String code;

    @ApiModelProperty(value = "参数键值")
    private String value;

    @ApiModelProperty(value = "参数扩展值")
    private String extValue;

    @ApiModelProperty(value = "系统内置（1是 0否）")
    private Integer isSystem;

    @ApiModelProperty(value = "参数名称")
    private String name;

    @ApiModelProperty(value = "参数详细描述")
    private String description;

    @ApiModelProperty(value = "是否启用（1是 0否）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否删除  0-正常 1-删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间，默认当前创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后修改人")
    private Long updateUser;


}
