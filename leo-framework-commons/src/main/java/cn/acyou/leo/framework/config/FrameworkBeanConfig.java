package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.annotation.valid.DictValue;
import cn.acyou.leo.framework.annotation.valid.ListValue;
import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import cn.acyou.leo.framework.constraintvalidators.DictValueConstraintValidator;
import cn.acyou.leo.framework.constraintvalidators.ListValueConstraintValidator;
import cn.acyou.leo.framework.constraintvalidators.PropertyScriptAssertValidator;
import cn.acyou.leo.framework.mybatis.PerformanceInterceptor;
import cn.acyou.leo.framework.mybatis.extend.CustomerSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author fangyou
 * @version [1.0.0, 2021-10-19 10:19]
 */
@EnableRetry
@EnableScheduling
@Configuration
public class FrameworkBeanConfig {
    @Bean
    public Validator getValidator() {
        HibernateValidatorConfiguration configuration = Validation
                .byProvider(HibernateValidator.class)
                .configure();
        /* 自定义Validator支持 */
        ConstraintMapping constraintMapping = configuration.createConstraintMapping();
        //集合类型包含校验
        constraintMapping.constraintDefinition(ListValue.class)
                .includeExistingValidators(false)
                .validatedBy(ListValueConstraintValidator.class);
        //数据字典类型校验
        constraintMapping.constraintDefinition(DictValue.class)
                .includeExistingValidators(false)
                .validatedBy(DictValueConstraintValidator.class);
        //全属性级别脚本判断
        constraintMapping.constraintDefinition(PropertyScriptAssert.class)
                .includeExistingValidators(false)
                .validatedBy(PropertyScriptAssertValidator.class);
        configuration.failFast(true);
        return configuration.addMapping(constraintMapping)
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * MybatisPlus自定义sql注入器
     */
    @Bean
    public ISqlInjector iSqlInjector() {
        return new CustomerSqlInjector();
    }

    @Bean("performanceInterceptor")
    @ConditionalOnProperty(value = "leo.debug.print-performance-sql", havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }


    /**
     * 自动创建数据库
     * <p/></p>
     * <pre>
     * ### 方法1：使用连接参数 createDatabaseIfNotExist=true
     *     url: jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
     *
     * ### 方法2： 参照DBCreateInitializer
     * </pre>
     **/
    @Bean
    @ConditionalOnProperty(value = "leo.auto-create-db", havingValue = "true")
    public DBCreateInitializer dbCreateInitializer(DataSource dataSource) throws Exception {
        return new DBCreateInitializer(dataSource);
    }
}
