package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 申请KEY {@see https://lbs.qq.com/dev/console/application/mine}
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.xxl-job")
public class XxlJobProperty {
    /**
     * 启用xxl-job
     */
    private Boolean enable;

    /**
     * admin地址 example: http://127.0.0.1:8080/xxl-job-admin
     */
    private String adminAddresses = "http://127.0.0.1:8080/xxl-job-admin";
    /**
     * adminToken example: default_token
     */
    private String adminToken = "default_token";
    /**
     * 执行器名称
     */
    private String appname = "xxl-job-executor";
}
