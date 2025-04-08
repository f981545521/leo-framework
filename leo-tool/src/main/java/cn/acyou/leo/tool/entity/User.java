package cn.acyou.leo.tool.entity;

import cn.acyou.leo.framework.annotation.Sensitive;
import cn.acyou.leo.tool.handler.StringListStringTypeHandler;
import cn.acyou.leo.tool.handler.global.JsonTypeHandler;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author youfang
 * @since 2022-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_user", autoResultMap = true)
@ApiModel(value = "User对象", description = "系统用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户签名")
    private String signature;

    @ApiModelProperty(value = "性别 1男 2女")
    private Integer sex;

    @ApiModelProperty(value = "用户手机号区号")
    private String areaCode;

    @ApiModelProperty(value = "用户手机号")
    @Sensitive(type = Sensitive.Type.PHONE)
    private String phone;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "用户来源")
    private SourceEnum source;

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "状态 0禁用 1正常")
    private Integer status;

    @ApiModelProperty(value = "账号过期时间")
    private Date expirationTime;

    @ApiModelProperty(value = "账号类型")
    private Integer type;

    @ApiModelProperty(value = "权限列表")
    @TableField(typeHandler = StringListStringTypeHandler.class)
    private List<String> perms;

    //{"point": 100, "growthValue": 100}
    //@ApiModelProperty(value = "详情扩展信息")
    //@TableField(typeHandler = JsonTypeHandler.class)
    //private UserSummaryDetails details;

    @ApiModelProperty(value = "详情扩展信息")
    @TableField(typeHandler = JsonTypeHandler.class)
    private JSONArray details;

    //@ApiModelProperty(value = "详情扩展信息")
    //@TableField(typeHandler = JsonTypeHandler.class)
    //private List<Long> details;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间,默认当前时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @AllArgsConstructor
    public enum SourceEnum {
        H5("H5", "H5网页"),
        PC("PC", "电脑端"),
        MINI_PROGRAM("MINI_PROGRAM", "小程序端"),
        WECHAT("WECHAT", "微信公众号"),
        ANDROID("ANDROID", "安卓APP"),
        IOS("IOS","苹果APP");

        @EnumValue
        private final String name;

        //@JsonValue
        private final String description;

        @JsonValue
        private Map<String, Object> info(){
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("value", this.name);
            Map<String, Object> options = new LinkedHashMap<>();
            for (SourceEnum value : SourceEnum.values()) {
                options.put(value.name(), value.description);
            }
            map.put("options", options);
            return map;
        }

    }

    @Data
    public static class UserSummaryDetails {
        @ApiModelProperty(value = "积分")
        private Integer point;

        @ApiModelProperty(value = "成长值")
        private Integer growthValue;

    }

}
