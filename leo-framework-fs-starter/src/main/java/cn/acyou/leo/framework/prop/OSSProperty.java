package cn.acyou.leo.framework.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-01 10:56]
 */
@ConfigurationProperties(prefix = "oss")
public class OSSProperty {
    /**
     * endpoint
     */
    private String endpoint;
    /**
     * access_key_id
     */
    private String access_key_id;
    /**
     * access_key_secret
     */
    private String access_key_secret;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccess_key_id() {
        return access_key_id;
    }

    public void setAccess_key_id(String access_key_id) {
        this.access_key_id = access_key_id;
    }

    public String getAccess_key_secret() {
        return access_key_secret;
    }

    public void setAccess_key_secret(String access_key_secret) {
        this.access_key_secret = access_key_secret;
    }
}
