package cn.acyou.leo.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交
 *
 * @author fangyou
 * @version [1.0.0, 2021-09-26 17:57]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AccessLimit {
    /**
     * 访问最低间隔 (单位：毫秒)
     *
     * @return 间隔毫秒数
     */
    long interval() default 1000;

    /**
     * 参数表达式 (相同参数的才会被拦截)
     *
     * @return boolean
     */
    String value();
}
