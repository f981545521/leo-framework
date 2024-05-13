package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.email")
public class EmailProperty {
    /**
     * 替换为你的邮箱用户名
     */
    private String username;
    /**
     * 替换为你的邮箱密码
     */
    private String password;
    private String smtpAuth;
    private String smtpHost;
    private String smtpPort;
}
