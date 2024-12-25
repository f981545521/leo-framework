package cn.acyou.leo.tool.dto.dict;

import cn.acyou.leo.framework.model.PageSo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    /**
     *   "createDates":["2024-12-01","2024-12-25"]
     */
    @ApiModelProperty("创建时间查询")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private List<Date> createDates;

    /**
     * Mybatis参数错误
     *
     * It was either not specified and/or could not be found for the javaType ([Ljava.util.Date;) : jdbcType (null) combination.
     *
     * 需要特殊处理转换类型
     * <if test="createDateArray!=null and createDateArray.length== 2">and create_time between #{createDateArray[0],javaType=java.util.Date,jdbcType=TIMESTAMP} and #{createDateArray[1],javaType=java.util.Date,jdbcType=TIMESTAMP}</if>
     *
     *
     */
    @ApiModelProperty("创建时间查询")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date[] createDateArray;

    @ApiModelProperty("创建时间查询")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date[] createDateArray2;
}
