package cn.acyou.leo.framework.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 表字段信息
 * </p>
 *
 * @author youfang
 * @since 2025-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "表字段信息", description = "表字段信息")
public class TableFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段名称")
    private String Field;

    @ApiModelProperty(value = "字段类型 bigint varchar(50) int date datetime tinyint")
    private String Type;

    @ApiModelProperty(value = "是否可为空 YES NO")
    private String Null;

    @ApiModelProperty(value = "主键 PRI")
    private String Key;

    @ApiModelProperty(value = "默认值")
    private String Default;

    @ApiModelProperty(value = "扩展")
    private String Extra;

}
