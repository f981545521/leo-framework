package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.prop.ApiDocProperty;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author youfang
 * @version [1.0.0, 2021-7-11]
 **/
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@ConditionalOnProperty(value = "leo.api.enable", havingValue = "true")
@EnableConfigurationProperties({ApiDocProperty.class})
public class Knife4jEnhanceConfig {
    private final static String GROUP_NAME = "1.0";
    private OpenApiExtensionResolver openApiExtensionResolver;
    @Autowired
    private ApiDocProperty apiDocProperty;

    @Autowired(required = false)
    public void setEnhanceConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        if (openApiExtensionResolver != null) {
            this.openApiExtensionResolver = openApiExtensionResolver;
            log.info("[开发文档] 配置增强模式！");
        }
    }


    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        log.info("[开发文档] 初始化完成！");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(apiDocProperty.getTitle())
                        .description(apiDocProperty.getDescription())
                        .termsOfServiceUrl(apiDocProperty.getTermsOfServiceUrl())
                        .contact(apiDocProperty.getContact())
                        .version(apiDocProperty.getVersion())
                        .build())
                //分组名称
                .groupName(GROUP_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage(apiDocProperty.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
        if (openApiExtensionResolver != null) {
            docket.extensions(openApiExtensionResolver.buildExtensions(GROUP_NAME));
        }
        return docket;
    }
}
