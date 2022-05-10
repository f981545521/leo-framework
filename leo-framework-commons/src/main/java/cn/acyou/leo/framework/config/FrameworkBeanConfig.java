package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.annotation.valid.DictValue;
import cn.acyou.leo.framework.annotation.valid.ListValue;
import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import cn.acyou.leo.framework.constraintvalidators.DictValueConstraintValidator;
import cn.acyou.leo.framework.constraintvalidators.ListValueConstraintValidator;
import cn.acyou.leo.framework.constraintvalidators.PropertyScriptAssertValidator;
import cn.acyou.leo.framework.mybatis.extend.CustomerSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author fangyou
 * @version [1.0.0, 2021-10-19 10:19]
 */
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
        //全属性级别脚本判断
        constraintMapping.constraintDefinition(PropertyScriptAssert.class)
                .includeExistingValidators(false)
                .validatedBy(PropertyScriptAssertValidator.class);
        //数据字典类型校验
        constraintMapping.constraintDefinition(DictValue.class)
                .includeExistingValidators(false)
                .validatedBy(DictValueConstraintValidator.class);

        return configuration.addMapping(constraintMapping)
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * 自定义sql注入器
     * 关键部位重要的事情说三遍，不注入不生效，不注入不生效，不注入不生效
     */
    @Bean
    public ISqlInjector iSqlInjector() {
        return new CustomerSqlInjector();
    }

}
