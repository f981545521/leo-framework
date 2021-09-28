package cn.acyou.leo.framework.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

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

    @ApiModelProperty("用户拥有的角色")
    private Set<String> roleCodes;

    @ApiModelProperty("用户拥有的权限")
    private Set<String> permsList;

    @ApiModelProperty("所属组织")
    private OrganizationVo organization;

    @ApiModelProperty("角色")
    private RoleVo roleVo;



}
