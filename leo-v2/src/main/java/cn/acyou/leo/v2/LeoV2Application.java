package cn.acyou.leo.v2;

import cn.acyou.leo.framework.auto.EnableLeoFramework;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/17 11:01]
 **/
@EnableLeoFramework
@SpringBootApplication(scanBasePackages = "cn.acyou.leo")
@MapperScan({"cn.acyou.leo.v2.mapper"})
public class LeoV2Application {

    public static void main(String[] args) {
        SpringApplication.run(LeoV2Application.class, args);
    }
}
