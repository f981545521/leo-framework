package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.prop.BaiDuShortLinkProperty;
import cn.acyou.leo.framework.prop.DingTalkProperty;
import cn.acyou.leo.framework.util.component.BaiDuShortLinkUtil;
import cn.acyou.leo.framework.util.component.DingTalkUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:04]
 **/
@Configuration
@EnableConfigurationProperties({BaiDuShortLinkProperty.class, DingTalkProperty.class})
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
}
