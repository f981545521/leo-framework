package cn.acyou.leo.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 地区信息表
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_area")
@ApiModel(value = "Area对象", description = "地区信息表")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区划ID")
    private String id;

    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ApiModelProperty(value = "全称")
    private String name;

    @ApiModelProperty(value = "全称聚合")
    private String mergerName;

    @ApiModelProperty(value = "简称")
    private String shortName;

    @ApiModelProperty(value = "简称聚合")
    private String mergerShortName;

    @ApiModelProperty(value = "级别")
    private String levelType;

    @ApiModelProperty(value = "区号")
    private String cityCode;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "简拼")
    private String jianpin;

    @ApiModelProperty(value = "首字母")
    private String firstChar;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "备注")
    private String remark;


}
