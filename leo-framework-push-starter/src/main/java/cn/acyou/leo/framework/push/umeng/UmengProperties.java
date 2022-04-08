package cn.acyou.leo.framework.push.umeng;

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
         *
         */
        public String appkey;
        /**
         *
         */
        public String appMasterSecret;
    }


}
