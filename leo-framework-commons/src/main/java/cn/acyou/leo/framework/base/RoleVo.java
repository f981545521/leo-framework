package cn.acyou.leo.framework.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/8]
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 797702331484942116L;

    private Long	roleId;
    /**
     * 角色编码
     */
    private String	roleCode;
    /**
     * 角色名
     */
    private String	roleName;
    /**
     * 对需要有权限的查询的数据范围
     */
    private Integer	dataScope;
    /**
     * 状态 0停用 1正常
     */
    private Integer	status;
    /**
     * 职能描述
     */
    private String	description;
    /**
     * 创建人
     */
    private Long	createUser;
    /**
     * 创建时间,默认当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改人
     */
    private Long	updateUser;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date	updateTime;

    /**
     * 角色成员数量
     */
    private Integer userCount;
}
