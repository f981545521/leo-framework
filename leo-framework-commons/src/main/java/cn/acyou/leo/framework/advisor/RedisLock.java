package cn.acyou.leo.framework.advisor;

import java.lang.annotation.*;

/**
 * 使用示例：
 * <pre>
 *     {@code
 *     @RedisLock(key = "#name")
 *     public String testRedisLock(String name){
 *         return "hello" + name;
 *     }
 *     }
 * </pre>
 * @author youfang
 * @version [1.0.0, 2021-11-28]
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * 过期时间 单位毫秒
     * @return 过期时间
     */
    int expire() default 60 * 1000;

    /**
     * 参数键值 必须
     *
     * @return {@link String}
     */
    String key();

    /**
     * 等待时间 单位毫秒
     *
     * @return int 等待时间 0 不等待
     */
    int waitTime() default 0;
}