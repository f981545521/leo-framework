package cn.acyou.leo.tool;

import cn.acyou.leo.framework.auto.EnableLeoFramework;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableLeoFramework
@SpringBootApplication(scanBasePackages = "cn.acyou.leo", exclude = {
        SpringBootConfiguration.class /*不使用shardingsphere的时候排除*/
})
@EnableFeignClients
@MapperScan({"cn.acyou.leo.tool.mapper"})
public class LeoToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeoToolApplication.class, args);
    }

}
