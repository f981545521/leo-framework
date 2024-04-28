package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.prop.*;
import cn.acyou.leo.framework.util.component.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:04]
 **/
@Configuration
@EnableConfigurationProperties({BaiDuShortLinkProperty.class, DingTalkProperty.class,
        TencentMapProperty.class, GaodeMapProperty.class, OpenApiProperty.class, TranslateProperty.class,
        LeoFullProperty.class
})
public class FrameworkToolConfig {

    @Bean
    @ConditionalOnProperty({"leo.tool.baidu-short-link.token"})
    public BaiDuShortLinkUtil baiDuShortLinkUtil(BaiDuShortLinkProperty baiDuShortLinkProperty) {
        return new BaiDuShortLinkUtil(baiDuShortLinkProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.tool.ding-talk.enable"})
    public DingTalkUtil dingTalkUtil(DingTalkProperty dingTalkProperty) {
        return new DingTalkUtil(dingTalkProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.tool.tencent-map-key.key"})
    public TencentMapUtil tencentMapUtil(TencentMapProperty tencentMapProperty) {
        return new TencentMapUtil(tencentMapProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.tool.gaode-map-key.key"})
    public GaodeMapUtil gaodeMapUtil(GaodeMapProperty gaodeMapProperty) {
        return new GaodeMapUtil(gaodeMapProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.tool.translate"})
    public TranslateUtil translateUtil(TranslateProperty translateProperty) {
        return new TranslateUtil(translateProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.tool.openapi"})
    public OpenApiUtil openApiUtil(OpenApiProperty openApiProperty) {
        return new OpenApiUtil(openApiProperty);
    }
}
