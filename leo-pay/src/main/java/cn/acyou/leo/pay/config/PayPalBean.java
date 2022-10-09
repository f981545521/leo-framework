package cn.acyou.leo.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "ijpay.paypal")
public class PayPalBean {
    private Map<String, PayPalConfig> configMap;
}
