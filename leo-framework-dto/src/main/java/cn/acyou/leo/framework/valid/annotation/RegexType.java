package cn.acyou.leo.framework.valid.annotation;

/**
 * 校验的正则表达式类型
 *
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 */
public enum RegexType {
    /**
     * 空
     */
    NONE,
    /**
     * 特殊字符
     */
    SPECIALCHAR,
    /**
     * 中文
     */
    CHINESE,
    /**
     * 电子邮箱
     */
    EMAIL,
    /**
     * ip
     */
    IP,
    /**
     * 数字
     */
    NUMBER,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 手机号或固定电话号
     */
    MOBILEPHONE_OR_TELEPHONE,
    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 日期
     */
    DATE,
    /**
     * 数字字母组合
     */
    NUMBER_LETTER_COMBINATION,
    /**
     * 数字字母符号组合
     */
    NUMBER_LETTER_SYMBOL_COMBINATION,
    /**
     * 时间(年月日时分秒)
     */
    DATETIME;
}
