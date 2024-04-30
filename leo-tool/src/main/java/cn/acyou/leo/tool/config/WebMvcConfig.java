package cn.acyou.leo.tool.config;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.interceptor.SpringMvcInterceptor;
import cn.acyou.leo.tool.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author youfang
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

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

    @Bean
    public List<User> dbUserList() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUserId(1L);
        user.setRoleId(1L);
        user.setUserName("admin");
        user.setSignature("");
        user.setSex(1);
        user.setPhone("10001");
        user.setAge(1);
        user.setBirthday(LocalDate.now());
        user.setPassword("10001");
        user.setStatus(Constant.ENABLE);
        user.setPerms("tool:test:*");
        user.setCreateUser(1L);
        user.setCreateTime(new Date());
        user.setUpdateUser(1L);
        user.setUpdateTime(new Date());
        userList.add(user);
        //2
        User user2 = new User();
        user2.setUserId(2L);
        user2.setRoleId(1L);
        user2.setUserName("admin");
        user2.setSignature("");
        user2.setSex(1);
        user2.setPhone("10002");
        user2.setAge(1);
        user2.setBirthday(LocalDate.now());
        user2.setPassword("10002");
        user2.setStatus(Constant.DISABLED);
        user2.setCreateUser(1L);
        user2.setCreateTime(new Date());
        user2.setUpdateUser(1L);
        user2.setUpdateTime(new Date());
        userList.add(user2);
        //3
        User user3 = new User();
        user3.setUserId(3L);
        user3.setRoleId(2L);
        user3.setUserName("admin");
        user3.setSignature("");
        user3.setSex(1);
        user3.setPhone("10002");
        user3.setAge(1);
        user3.setBirthday(LocalDate.now());
        user3.setPassword("10002");
        user3.setStatus(Constant.ENABLE);
        user3.setCreateUser(1L);
        user3.setCreateTime(new Date());
        user3.setUpdateUser(1L);
        user3.setUpdateTime(new Date());
        userList.add(user3);
        //...
        return userList;
    }


}
