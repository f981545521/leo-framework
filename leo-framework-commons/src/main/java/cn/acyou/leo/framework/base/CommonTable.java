package cn.acyou.leo.framework.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 公共表
 * </p>
 *
 * @author youfang
 * @since 2024-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "公共表对象", description = "公共表")
public class CommonTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数主键(PK)")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "简称")
    private String abbr;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "描述")
    private String text;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "金额")
    private BigDecimal price;

    @ApiModelProperty(value = "类型")
    private Integer type;

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String field5;

    private BigDecimal numberField1;
    private BigDecimal numberField2;
    private BigDecimal numberField3;
    private BigDecimal numberField4;
    private BigDecimal numberField5;

    private String field6;

    private String field7;

    private String field8;

    private String field9;

    private String field10;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否删除  0-正常 1-删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间，默认当前创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后修改人")
    private Long updateUser;


}
