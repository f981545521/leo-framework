package cn.acyou.leo.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/17 10:09]
 **/
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@MapperScan(basePackages = "cn.acyou.leo.framework.mapper")
public class MybatisScanExtRegister {

}