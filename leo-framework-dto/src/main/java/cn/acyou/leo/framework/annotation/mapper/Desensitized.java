package cn.acyou.leo.framework.annotation.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏
 * @author fangyou
 * @version [1.0.0, 2021-10-20 11:15]
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitized {

    /**
     * 敏感的类型
     *
     * @return 敏感的类型
     */
    SensitizedType type();
}
