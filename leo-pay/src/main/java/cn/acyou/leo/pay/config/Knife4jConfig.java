package cn.acyou.leo.pay.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Profile({"dev", "test"})
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    private final static String GROUP_NAME = "1.0";
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public Knife4jConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Leo pay 接口文档")
                        .description("# 接口调试页面。✍")
                        .termsOfServiceUrl("http://www.acyou.cn/")
                        .contact("youfang@acyou.cn")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName(GROUP_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.acyou.leo.pay.controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(GROUP_NAME));
        return docket;
    }
}
