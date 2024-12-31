package cn.acyou.leo.tool.dto.dict;

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
    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    private String name;
    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空")
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

    private String exThrow;
}
