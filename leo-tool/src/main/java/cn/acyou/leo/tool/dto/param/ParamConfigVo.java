package cn.acyou.leo.tool.dto.param;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(value="ParamConfig对象", description="参数配置表")
public class ParamConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "命名空间/组")
    private String namespace;

    @ApiModelProperty(value = "参数编码(键名)")
    private String code;

    @ApiModelProperty(value = "参数键值")
    private String value;

    @JsonIgnore
    @ApiModelProperty(value = "参数扩展值")
    private String extValue;

    @ApiModelProperty(value = "参数名称")
    private String name;

    @ApiModelProperty(value = "参数详细描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间，默认当前创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后修改人")
    private Long updateUser;

    @ApiModelProperty(value = "配置扩展值 JSON")
    private Object extValueJson;

    public Object getExtValueJson() {
        try {
            if (extValue == null || extValue.isEmpty()) {
                return null;
            }
            return JSON.parse(this.extValue);
        } catch (Exception e) {
            //ignore
        }
        return null;
    }
}
