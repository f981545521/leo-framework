package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/25 9:56]
 **/
public class ExtendEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final Log log;

    public ExtendEnvironmentPostProcessor(DeferredLogFactory logFactory){
        log = logFactory.getLog(ExtendEnvironmentPostProcessor.class);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            MutablePropertySources propertySources = environment.getPropertySources();
            //项目resources扩展
            ClassPathResource extendProperties = new ClassPathResource("extendProperties");
            if (extendProperties.exists()) {
                File extendPropertiesDir = extendProperties.getFile();
                if (extendPropertiesDir.exists()) {
                    File[] files = extendPropertiesDir.listFiles();
                    if (files != null && files.length > 0) {
                        for (File listFile : files) {
                            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("extendProperties" + File.separator + listFile.getName()));
                            propertySources.addFirst(new PropertiesPropertySource("extendConfig_" + listFile.getName(), properties));
                            log.info("扩展配置 加载成功！ 路径：[" + listFile.getPath() + "]");
                        }
                    }
                }
            }
            //外部文件扩展
            String propertyPath = environment.getProperty("leo.extend-properties-path");
            if (StringUtils.isNotBlank(propertyPath)) {
                Properties properties = new Properties();
                if (propertyPath.startsWith("http")) {
                    properties = PropertiesLoaderUtils.loadProperties(new FileUrlResource(new URL(propertyPath)));
                } else {
                    properties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(propertyPath));
                }
                propertySources.addFirst(new PropertiesPropertySource("extendConfig", properties));
            }
            log.info("扩展配置 加载成功！ 路径：[" + propertyPath + "]");
        } catch (Exception e) {
            log.error("扩展配置 加载失败！ 原因：" + e.getClass().getName() + "[" + e.getMessage() + "]");
        }
    }
}

