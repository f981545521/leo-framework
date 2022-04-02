package cn.acyou.leo.framework.retrofit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/14 16:34]
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RetrofitApi {

    /**
     * API 说明文档
     *
     * @return API 说明
     */
    String value();

    /**
     * 检查结果
     * 为true时，result为false将抛出异常
     *
     * @return boolean
     */
    boolean checkResult() default false;

    /**
     * 响应长度
     *
     * @return int
     */
    int responseLength() default 2000;


}
