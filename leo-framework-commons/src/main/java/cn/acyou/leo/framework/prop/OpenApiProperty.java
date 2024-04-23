package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.openapi")
public class OpenApiProperty {

    /**
     * 阅读和风天气开发文档  https://dev.qweather.com/docs/
     */
    private String qweatherKey;
}
