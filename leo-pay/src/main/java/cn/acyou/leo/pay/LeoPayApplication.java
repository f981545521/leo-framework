package cn.acyou.leo.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.acyou.leo")
public class LeoPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoPayApplication.class, args);
    }

}
