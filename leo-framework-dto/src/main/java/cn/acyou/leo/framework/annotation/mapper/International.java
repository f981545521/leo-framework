package cn.acyou.leo.framework.annotation.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 国际化
 * @author fangyou
 * @version [1.0.0, 2021-10-15 10:49]
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface International {

    /**
     * 分隔符(默认为 |)
     */
    String separator() default "\\|";


}
