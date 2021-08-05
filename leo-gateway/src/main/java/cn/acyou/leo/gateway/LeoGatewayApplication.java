package cn.acyou.leo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LeoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoGatewayApplication.class, args);
    }

}
