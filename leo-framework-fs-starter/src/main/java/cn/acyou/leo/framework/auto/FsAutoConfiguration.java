package cn.acyou.leo.framework.auto;

import cn.acyou.leo.framework.minio.MinIoUtil;
import cn.acyou.leo.framework.prop.MinIoProperty;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/31 17:01]
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = MinIoProperty.class)
public class FsAutoConfiguration {

    @Bean
    @ConditionalOnProperty({"leo.fs.minio.endpoint", "leo.fs.minio.access-key", "leo.fs.minio.secret-key"})
    public MinioClient minioClient(MinIoProperty minIoProperty){
        return MinioClient.builder()
                .endpoint(minIoProperty.getEndpoint())
                .credentials(minIoProperty.getAccessKey(), minIoProperty.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean(MinioClient.class)
    public MinIoUtil minIoUtil(MinioClient minioClient){
        return new MinIoUtil(minioClient);
    }
}
