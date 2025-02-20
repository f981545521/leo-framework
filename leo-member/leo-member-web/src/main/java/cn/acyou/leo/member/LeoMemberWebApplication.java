package cn.acyou.leo.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "cn.acyou.leo")
@EnableDiscoveryClient
public class LeoMemberWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoMemberWebApplication.class, args);
    }

}
