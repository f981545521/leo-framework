package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.net.URL;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/25 9:56]
 **/
@Slf4j
public class ExtendEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            String propertyPath = environment.getProperty("leo.extend-properties-path");
            if (StringUtils.isNotBlank(propertyPath)) {
                Properties properties = new Properties();
                if (propertyPath.startsWith("http")) {
                    properties = PropertiesLoaderUtils.loadProperties(new FileUrlResource(new URL(propertyPath)));
                } else {
                    properties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(propertyPath));
                }
                MutablePropertySources propertySources = environment.getPropertySources();
                propertySources.addFirst(new PropertiesPropertySource("extendConfig", properties));
            }
        } catch (Exception e) {
            log.error("扩展配置加载失败：{}", e.getMessage());
        }
    }
}

