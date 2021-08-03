package cn.acyou.leo.framework.valid.annotation;

/**
 * @author youfang
 * @version [1.0.0, 2020-3-21 下午 09:24]
 **/
public enum DateValidType {
    /**
     * 无规则
     */
    none,
    /**
     * 现在之前
     */
    if_beforeNow,
    /**
     * 现在之后
     */
    if_afterNow,
    /**
     * 指定日期之前
     */
    if_beforeSpecifyDate,
    /**
     * 指定日期之后
     */
    if_afterSpecifyDate;


}
