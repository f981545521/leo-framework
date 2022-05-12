package cn.acyou.leo.pay;

import cn.acyou.leo.framework.auto.EnableLeoFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableLeoFramework
//@MapperScan("cn.acyou.leo.pay.mapper")
@SpringBootApplication(scanBasePackages = "cn.acyou.leo", exclude = {RedisAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class LeoPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoPayApplication.class, args);
    }

}
