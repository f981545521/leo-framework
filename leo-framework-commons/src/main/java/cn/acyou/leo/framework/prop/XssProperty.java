package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-17]
 **/
@Data
@ConfigurationProperties(prefix = "leo.xss")
public class XssProperty {

    /**
     * 开启XSS过滤
     */
    private boolean enabled = true;
    /**
     * XSS过滤排除URL
     */
    private String excludes = "";

}