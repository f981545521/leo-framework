package cn.acyou.leo.framework.annotation.authz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-27 14:59]
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    /**
     * 权限标识
     */
    String[] value();

    /**
     * 关系
     */
    Logical logical() default Logical.AND;

}
