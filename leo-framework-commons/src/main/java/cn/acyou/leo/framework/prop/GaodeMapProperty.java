package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 申请KEY {@see https://lbs.qq.com/dev/console/application/mine}
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.gaode-map-key")
public class GaodeMapProperty {

    /**
     * 授权Key
     */
    private String key;
}
