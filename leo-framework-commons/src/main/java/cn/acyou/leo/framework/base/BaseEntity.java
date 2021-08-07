package cn.acyou.leo.framework.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/7]
 **/
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人
     */
    @Column(name = "create_user")
    private Long	createUser;
    /**
     * 最后修改时间
     */
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date	updateTime;
    /**
     * 最后修改人
     */
    @Column(name = "update_user")
    private Long	updateUser;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}
