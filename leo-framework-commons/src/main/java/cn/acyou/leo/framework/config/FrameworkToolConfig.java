package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.prop.BaiDuShortLinkProperty;
import cn.acyou.leo.framework.prop.DingTalkProperty;
import cn.acyou.leo.framework.prop.TencentMapProperty;
import cn.acyou.leo.framework.util.component.BaiDuShortLinkUtil;
import cn.acyou.leo.framework.util.component.DingTalkUtil;
import cn.acyou.leo.framework.util.component.TencentMapUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:04]
 **/
@Configuration
@EnableConfigurationProperties({BaiDuShortLinkProperty.class, DingTalkProperty.class, TencentMapProperty.class})
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
}
