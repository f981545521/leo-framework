package cn.acyou.leo.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ijpay.paypal")
public class PayPalBean {
    private String clientId;
    private String secret;
    private Boolean sandBox;
    private String domain;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Boolean getSandBox() {
        return sandBox;
    }

    public void setSandBox(Boolean sandBox) {
        this.sandBox = sandBox;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "PayPalBean{" +
                "clientId='" + clientId + '\'' +
                ", secret='" + secret + '\'' +
                ", sandBox=" + sandBox +
                ", domain='" + domain + '\'' +
                '}';
    }
}