package cn.acyou.leo.tool.dto.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/16]
 **/
@Data
public class DictSaveReq implements Serializable {
    private Long id;

    @NotBlank(message = "字典名称不能为空")
    @ApiModelProperty("字典名称")
    private String name;

    @NotBlank(message = "字典编码不能为空")
    @ApiModelProperty("字典编码")
    private String code;

    @ApiModelProperty(value = "父节点ID", notes = "如果是0代表是顶级字典")
    private Long parentId;

    @ApiModelProperty(value = "排序值", hidden = true)
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "状态", notes = " 1-正常")
    private Integer status;

    private String exThrow;
}
