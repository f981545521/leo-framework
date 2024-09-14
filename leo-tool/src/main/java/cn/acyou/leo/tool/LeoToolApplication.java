package cn.acyou.leo.tool;

import cn.acyou.leo.framework.auto.EnableLeoFramework;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableLeoFramework
@SpringBootApplication(scanBasePackages = "cn.acyou.leo")
@EnableFeignClients
@MapperScan({"cn.acyou.leo.tool.mapper"})
public class LeoToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoToolApplication.class, args);
    }

}
