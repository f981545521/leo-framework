package cn.acyou.leo.framework.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口调用统计
 * @author youfang
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceCallStatistics implements Serializable{

    private static final long serialVersionUID = -7344988122198709701L;
	/**
	 * 主键ID
	 */
    private Long	id;
	/**
	 * 调用接口URL
	 */
    private String	url;
	/**
	 * 类型 1-自动
	 */
    private Integer	type;
	/**
	 * 自定义描述信息
	 */
    private String	description;
	/**
	 * 方法类型
	 */
    private String	methodType;
	/**
	 * 调用接口信息
	 */
    private String	methodInfo;
	/**
	 * 调用接口描述
	 */
    private String	methodDesc;
	/**
	 * 接口请求参数
	 */
    private String	params;
	/**
	 * 调用时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date	startTime;
	/**
	 * 执行时间(单位：毫秒)
	 */
    private Long	execTime;
	/**
	 * 错误信息
	 */
    private String	errorMessage;
	/**
	 * 错误堆栈
	 */
    private String	errorStackTrace;
	/**
	 * 终端类型(1:WEB，2:微信小程序，3:PDA)
	 */
    private String	clientType;
	/**
	 * 调用者IP地址
	 */
    private String	ip;
	/**
	 * 调用用户ID
	 */
    private Long	userId;
	/**
	 * 调用用户姓名
	 */
    private String	userName;


}
