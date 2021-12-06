package cn.acyou.leo.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 17:16]
 */
@Profile({"dev", "test"})
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestfulApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //单包扫描
                .apis(RequestHandlerSelectors.basePackage("cn.acyou.leo.pay.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Leo pay 接口文档")
                .description("接口调试页面。✍")
                //联系人
                .contact(new Contact("youfang", "http://www.acyou.cn", "youfang@acyou.cn"))
                .version("1.0.0")
                .build();
    }
}
