package cn.acyou.leo.framework.commons.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/6/30]
 **/
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1530988057621544478L;

    @ApiModelProperty("登录token")
    private String token;

    @ApiModelProperty("用户id")
    private Long	userId;

    @ApiModelProperty("用户名")
    private String	userName;

}
