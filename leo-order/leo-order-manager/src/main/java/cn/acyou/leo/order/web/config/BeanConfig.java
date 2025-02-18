package cn.acyou.leo.order.web.config;

import cn.acyou.leo.framework.commons.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-4 下午 09:32]
 **/
@Configuration
public class BeanConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(1, 1);
    }

}
