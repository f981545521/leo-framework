package cn.acyou.leo.framework.auto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author youfang
 * @version [1.0.0, 2022/1/19 16:16]
 **/
@Configuration
@ComponentScan("cn.acyou.leo.framework")
@MapperScan(basePackages = "cn.acyou.leo.framework.mapper")
public class LeoFrameworkConfiguration {

}
