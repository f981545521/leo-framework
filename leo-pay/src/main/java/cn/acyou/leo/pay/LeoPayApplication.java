package cn.acyou.leo.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.acyou.leo")
@MapperScan("cn.acyou.leo.pay.mapper")
public class LeoPayApplication {

    public static void main(String[] args) {
        System.setProperty("pagehelper.banner", "false");
        SpringApplication.run(LeoPayApplication.class, args);
    }

}
