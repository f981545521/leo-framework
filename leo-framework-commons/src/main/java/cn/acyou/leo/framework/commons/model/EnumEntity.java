package cn.acyou.leo.framework.commons.model;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class EnumEntity implements Serializable {

    private static final long serialVersionUID = -444792519303908950L;
    /**
     * 编码
     */
    public String code;
    /**
     * 名称
     */
    public String name;
    /**
     * 备注
     */
    public String remark;

    public EnumEntity() {
    }

    public EnumEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public EnumEntity(Integer code, String name) {
        this.code = String.valueOf(code);
        this.name = name;
    }

    public EnumEntity(String code, String name, String remark) {
        this.code = code;
        this.name = name;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public Integer getIntegerCode() {
        return Integer.valueOf(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "StatusEntity{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
