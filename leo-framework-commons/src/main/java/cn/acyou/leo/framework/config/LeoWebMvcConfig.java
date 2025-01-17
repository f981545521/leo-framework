package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.interceptor.SpringMvcInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author youfang
 */
@Configuration
@Slf4j
public class LeoWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //这里是Web容器层次的跨域
        //registry.addMapping("/**")
        //        .allowCredentials(false)
        //        .allowedOriginPatterns("*")
        //        .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
        //        .allowedHeaders("*")
        //        .exposedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(springMvcInterceptor());
    }

    @Bean
    public SpringMvcInterceptor springMvcInterceptor() {
        return new SpringMvcInterceptor();
    }


    /**
     * 引入jackson-dataformat-xml后 去除XML的消息转换器
     * https://hafuhafu.com/archives/springboot-jackson-xml-result-json/
     *
     * @param converters HttpMessageConverter
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2XmlHttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


    }



}
