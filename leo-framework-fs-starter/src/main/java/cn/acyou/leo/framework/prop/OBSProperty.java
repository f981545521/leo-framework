package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-01 10:56]
 */
@Data
@ConfigurationProperties(prefix = "leo.fs.obs")
public class OBSProperty {
    /**
     * obs endpoint
     */
    private String endpoint;
    /**
     * obs access_key_id
     */
    private String accessKeyId;
    /**
     * obs access_key_secret
     */
    private String accessKeySecret;

}
