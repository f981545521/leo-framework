package cn.acyou.leo.tool.dto.dict;

import cn.acyou.leo.framework.model.PageSo;
import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/16]
 **/
@Data
public class DictSo extends PageSo {
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
     * 状态:  1-正常
     */
    private Integer status;
}
