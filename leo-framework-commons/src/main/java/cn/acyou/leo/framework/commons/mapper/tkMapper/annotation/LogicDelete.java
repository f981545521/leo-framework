package cn.acyou.leo.framework.commons.mapper.tkMapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 支持逻辑删除
 *
 * @author youfang
 * @version [1.0.0, 2020/4/22]
 **/
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicDelete {
    /**
     * 逻辑未删除值(默认为 0)
     */
    int notDeletedVal() default 0;

    /**
     * 逻辑已删除值(默认为 1)
     */
    int deletedVal() default 1;

}