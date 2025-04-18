package cn.acyou.leo.v1;

import cn.acyou.leo.framework.auto.EnableLeoFramework;
import cn.acyou.leo.framework.config.MybatisScanExtRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/15 18:18]
 **/
@EnableLeoFramework
@SpringBootApplication(scanBasePackages = "cn.acyou.leo",exclude = {RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, MybatisScanExtRegister.class})
//@MapperScan({"cn.acyou.leo.v1.mapper"})
public class LeoV1Application {

    public static void main(String[] args) {
        SpringApplication.run(LeoV1Application.class, args);
    }
}
