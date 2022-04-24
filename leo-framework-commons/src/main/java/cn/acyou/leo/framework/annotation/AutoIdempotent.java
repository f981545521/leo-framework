package cn.acyou.leo.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 强幂等性，需要先调用接口生产序列号:`sequence`
 *
 * @author youfang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutoIdempotent {
    /**
     * 缓存key的前缀
     * <p>
     * TODO: 思考：是否需要这个前缀
     *
     * @return 自定义缓存前缀
     */
    String prefix() default "DEFAULT";

}
