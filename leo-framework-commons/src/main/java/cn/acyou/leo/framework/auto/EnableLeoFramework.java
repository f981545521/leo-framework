package cn.acyou.leo.framework.auto;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启自动配置
 * @author youfang
 * @version [1.0.0, 2022/1/19 16:15]
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LeoFrameworkConfiguration.class)
@Documented
public @interface EnableLeoFramework {

}
