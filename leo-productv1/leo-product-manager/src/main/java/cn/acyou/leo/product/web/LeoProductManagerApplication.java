package cn.acyou.leo.product.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@ComponentScan(value = "cn.acyou.leo")
@EnableDiscoveryClient
@EnableElasticsearchRepositories("cn.acyou.leo.product.es")
public class LeoProductManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoProductManagerApplication.class, args);
    }

}
