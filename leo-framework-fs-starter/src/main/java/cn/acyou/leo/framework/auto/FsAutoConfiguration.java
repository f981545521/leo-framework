package cn.acyou.leo.framework.auto;

import cn.acyou.leo.framework.ftp.FTPUtil;
import cn.acyou.leo.framework.minio.MinIoUtil;
import cn.acyou.leo.framework.obs.OBSUtil;
import cn.acyou.leo.framework.oss.OSSUtil;
import cn.acyou.leo.framework.prop.FTPProperty;
import cn.acyou.leo.framework.prop.MinIoProperty;
import cn.acyou.leo.framework.prop.OBSProperty;
import cn.acyou.leo.framework.prop.OSSProperty;
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
@EnableConfigurationProperties({MinIoProperty.class, OSSProperty.class, OBSProperty.class, FTPProperty.class})
public class FsAutoConfiguration {

    @Bean
    @ConditionalOnProperty({"leo.fs.minio.endpoint", "leo.fs.minio.access-key", "leo.fs.minio.secret-key"})
    public MinioClient minioClient(MinIoProperty minIoProperty) {
        return MinioClient.builder()
                .endpoint(minIoProperty.getEndpoint())
                .credentials(minIoProperty.getAccessKey(), minIoProperty.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean(MinioClient.class)
    public MinIoUtil minIoUtil(MinioClient minioClient) {
        return new MinIoUtil(minioClient);
    }

    @Bean
    @ConditionalOnProperty({"leo.fs.oss.endpoint", "leo.fs.oss.access-key-id", "leo.fs.oss.access-key-secret"})
    public OSSUtil ossUtil(OSSProperty ossProperty) {
        return new OSSUtil(ossProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.fs.obs.endpoint", "leo.fs.obs.access-key-id", "leo.fs.obs.access-key-secret"})
    public OBSUtil ossUtil(OBSProperty obsProperty) {
        return new OBSUtil(obsProperty);
    }

    @Bean
    @ConditionalOnProperty({"leo.fs.ftp.host", "leo.fs.ftp.port"})
    public FTPUtil ftpUtil(FTPProperty ftpProperty) {
        return new FTPUtil(ftpProperty);
    }
}
