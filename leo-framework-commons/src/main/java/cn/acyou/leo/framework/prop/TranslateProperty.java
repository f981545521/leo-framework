package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 获取：
 * 【有道】https://ai.youdao.com/DOCSIRMA/html/trans/api/wbfy/index.html#section-9
 * 【百度】https://api.fanyi.baidu.com/product/113
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.translate")
public class TranslateProperty {

    /**
     * 有道 APP_KEY
     */
    private String youdaoAppKey;

    /**
     * 有道 APP_KEY
     */
    private String youdaoAppSecret;

    /**
     * 百度 AppId
     */
    private String baiduAppId;

    /**
     * 百度 SecurityKey
     */
    private String baiduSecurityKey;
}
