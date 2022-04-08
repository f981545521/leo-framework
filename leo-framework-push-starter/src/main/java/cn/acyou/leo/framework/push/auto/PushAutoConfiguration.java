package cn.acyou.leo.framework.push.auto;

import cn.acyou.leo.framework.push.prop.UmengProperties;
import cn.acyou.leo.framework.push.umeng.PushUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置Bean
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-03 17:59]
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UmengProperties.class)
public class PushAutoConfiguration {

    /**
     * 友盟推送工具配置
     *
     * @param umengProperties 配置属性
     * @return PushUtils
     */
    @Bean
    @ConditionalOnProperty(value = "leo.umeng.enable", havingValue = "true")
    public PushUtils pushUtils(UmengProperties umengProperties) {
        return new PushUtils(umengProperties);
    }

}
