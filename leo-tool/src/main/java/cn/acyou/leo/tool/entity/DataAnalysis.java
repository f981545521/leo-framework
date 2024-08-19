package cn.acyou.leo.tool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据统计
 * </p>
 *
 * @author youfang
 * @since 2024-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DataAnalysis对象", description="数据统计")
public class DataAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "统计日期")
    private LocalDate dayTime;

    @ApiModelProperty(value = "数据JSON")
    private String content;

    @ApiModelProperty(value = "数据JSON(新用户)")
    private String contentNew;

    @ApiModelProperty(value = "扩展字段1")
    private String field1;

    @ApiModelProperty(value = "扩展字段2")
    private String field2;

    @ApiModelProperty(value = "扩展字段3")
    private String field3;

    @ApiModelProperty(value = "平台 guixiu(硅秀H5)/snap(Snap APP)")
    private String platform;

    @ApiModelProperty(value = "扩展参数JSON")
    private String ext;

    @ApiModelProperty(value = "删除标识 0.有效 1.无效")
    private Integer delFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
