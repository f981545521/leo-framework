package cn.acyou.leo.pay.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer
 * 由于MapperScannerConfigurer执行的比较早，所以必须在MyBatisConfigurer配置完成后执行
 * @author youfang
 * @date 2018-04-24 13:21
 */
@Configuration
@AutoConfigureAfter(MyBatisConfigurer.class)
public class MyBatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("cn.acyou.leo.pay.mapper");
        Properties properties = new Properties();
        //官方配置说明：{@link https://gitee.com/free/Mapper/wikis/Home?sort_id=14104}
        //在 4.0 以前这是一个非常重要的参数
        //4.0 之后，增加了一个 @RegisterMapper 注解，通用 Mapper 中提供的所有接口都有这个注解，有了该注解后，通用 Mapper 会自动解析所有的接口，如果父接口（递归向上找到的最顶层）存在标记该注解的接口，就会自动注册上。
        properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
        //取回主键的方式
        properties.setProperty("IDENTITY", "MYSQL");
        //insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        properties.setProperty("notEmpty", "true");
        //properties.setProperty("ORDER", "BEFORE");
        //实体和表转换时的默认规则：驼峰转下划线小写形式
        properties.setProperty("style", "camelhumpAndLowercase");
        //默认 false 用于校验通用 Example 构造参数 entityClass 是否和当前调用的 Mapper<EntityClass> 类型一致。
        properties.setProperty("checkExampleEntityClass", "true");
        //配置为 true 后，delete 和 deleteByExample 都必须设置查询条件才能删除，否则会抛出异常。
        properties.setProperty("safeDelete", "true");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
