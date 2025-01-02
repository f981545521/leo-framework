package cn.acyou.leo.framework.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
@Data
public class EnumEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 枚举名称
     */
    private String name;
    /**
     * 枚举编码
     */
    public String code;
    /**
     * 枚举描述
     */
    public String description;
    /**
     * 枚举备注
     */
    public String remark;

    public EnumEntity() {
    }

    public EnumEntity(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
