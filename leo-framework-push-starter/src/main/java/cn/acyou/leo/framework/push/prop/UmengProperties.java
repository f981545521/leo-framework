package cn.acyou.leo.framework.push.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 友盟推送配置信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "leo.umeng")
public class UmengProperties {

    /**
     * 开启
     */
    private boolean enable;
    /**
     * 是否测试通知
     */
    private boolean test;

    /**
     * IOS
     */
    public Config ios;

    /**
     * Android
     */
    public Config android;


    @Data
    public static class Config {
        /**
         * appkey
         */
        public String appkey;
        /**
         * appMasterSecret
         */
        public String appMasterSecret;
    }


}
