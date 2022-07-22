package cn.acyou.leo.tool.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.annotation.InterceptMark;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;

import java.lang.annotation.*;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/22 10:58]
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@InterceptMark
public @interface AccessToken {

    /**
     * 密钥Token
     * 支持占位符形式配置。
     *
     * @return accessToken
     */
    String accessToken();

    /**
     * 拦截器匹配路径
     *
     * @return
     */
    String[] include() default {"/**"};

    /**
     * 拦截器排除匹配，排除指定路径拦截
     *
     * @return
     */
    String[] exclude() default {};

    /**
     * 处理该注解的拦截器类
     * 优先从spring容器获取对应的Bean，如果获取不到，则使用反射创建一个！
     *
     * @return
     */
    Class<? extends BasePathMatchInterceptor> handler() default AccessTokenInterceptor.class;
}
