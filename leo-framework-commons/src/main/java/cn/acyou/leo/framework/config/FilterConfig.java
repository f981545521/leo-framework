package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.prop.XssProperty;
import cn.acyou.leo.framework.util.StringUtil;
import cn.acyou.leo.framework.xss.XssFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/13]
 **/
@Configuration
@EnableConfigurationProperties(value = XssProperty.class)
public class FilterConfig {

    /**
     * XSS Filter
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<?> xssFilterRegistration(XssProperty xssProperty) {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap<>();
        if (StringUtil.isNotNullOrBlank(xssProperty.getExcludes())) {
            initParameters.put("excludes", xssProperty.getExcludes());
        }
        if (StringUtil.isNotNullOrBlank(xssProperty.getEnabled())) {
            initParameters.put("enabled", xssProperty.getEnabled());
        }
        registration.setInitParameters(initParameters);
        return registration;
    }
}
