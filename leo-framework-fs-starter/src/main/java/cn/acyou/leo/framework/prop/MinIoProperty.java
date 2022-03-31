package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/31 16:46]
 **/
@Data
@ConfigurationProperties(prefix = "leo.fs.minio")
public class MinIoProperty {
    /**
     * endpoint
     */
    private String endpoint;
    /**
     * 访问Key
     */
    private String accessKey;
    /**
     * 访问密钥
     */
    private String secretKey;

}
