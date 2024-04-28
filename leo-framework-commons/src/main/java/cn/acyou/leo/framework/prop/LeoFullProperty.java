package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-17]
 **/
@Data
@ConfigurationProperties(prefix = "leo")
public class LeoFullProperty {

    /**
     * 扩展配置加载
     * <pre>
     * leo:
     *   extend-properties-paths: 'D:\workspace\work\private_account.properties'
     * </pre>
     */
    private String extendPropertiesPaths;

}