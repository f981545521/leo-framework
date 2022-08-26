package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.baidu-short-link")
public class BaiDuShortLinkProperty {

    /**
     * 授权TOKEN
     */
    private String token;
}
