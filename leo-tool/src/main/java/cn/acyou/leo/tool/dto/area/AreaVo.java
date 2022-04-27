package cn.acyou.leo.tool.dto.area;

import lombok.Data;

import java.io.Serializable;

/**
 * t_area 实体类
 * 2020-10-27 10:17:15 地区信息表
 *
 * @author youfang
 */
@Data
public class AreaVo implements Serializable {

	private static final long serialVersionUID = 4707339737503035267L;
	/**
	 * 区划ID
	 */
	private String id;
	/**
	 * 父ID
	 */
	private String parentId;
	/**
	 * 全称
	 */
	private String name;
	/**
	 * 全称聚合
	 */
	private String mergerName;
	/**
	 * 简称
	 */
	private String shortName;
	/**
	 * 简称聚合
	 */
	private String mergerShortName;
	/**
	 * 级别
	 */
	private String levelType;
	/**
	 * 区号
	 */
	private String cityCode;
	/**
	 * 邮编
	 */
	private String zipCode;
	/**
	 * 拼音
	 */
	private String pinyin;
	/**
	 * 简拼
	 */
	private String jianpin;
	/**
	 * 首字母
	 */
	private String firstChar;
	/**
	 * 经度
	 */
	private String lng;
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 备注
	 */
	private String remark;

}
