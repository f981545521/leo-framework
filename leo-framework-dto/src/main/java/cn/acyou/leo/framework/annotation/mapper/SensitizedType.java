package cn.acyou.leo.framework.annotation.mapper;


/**
 * 敏感类型
 *
 * @author fangyou
 * @version [1.0.0, 2021/10/20]
 */
public enum SensitizedType {
    /**
     * 无
     */
    none,
    /**
     * 手机号
     */
    mobilePhone,
    /**
     * 邮箱
     */
    email,
    /**
     * 真实姓名
     */
    chineseName,
    /**
     * 身份证号
     */
    idCardNum

}
