package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2022/2/22 9:32]
 **/
@Data
@ConfigurationProperties(prefix = "leo.api")
public class ApiDocProperty {
    /**
     * 启用开发文档
     */
    private Boolean enable;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档说明
     */
    private String description;
    /**
     * 访问地址
     */
    private String termsOfServiceUrl;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 版本
     */
    private String version;
    /**
     * 联系人
     */
    private String basePackage;
}
