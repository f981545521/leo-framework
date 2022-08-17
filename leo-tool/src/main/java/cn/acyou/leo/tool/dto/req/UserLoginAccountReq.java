package cn.acyou.leo.tool.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 账号密码登录请求参数
 *
 * @author youfang
 * @date 2020/07/02
 */
@Data
public class UserLoginAccountReq implements Serializable {

    private static final long serialVersionUID = -1227259089666973238L;

    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty("手机号码")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

}
