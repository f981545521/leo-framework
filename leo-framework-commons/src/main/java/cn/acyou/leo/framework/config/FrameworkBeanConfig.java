package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.annotation.valid.ListValue;
import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import cn.acyou.leo.framework.constraintvalidators.ListValueConstraintValidator;
import cn.acyou.leo.framework.constraintvalidators.PropertyScriptAssertValidator;
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
        constraintMapping.constraintDefinition(ListValue.class)
                .includeExistingValidators(false)
                .validatedBy(ListValueConstraintValidator.class);
        constraintMapping.constraintDefinition(PropertyScriptAssert.class)
                .includeExistingValidators(false)
                .validatedBy(PropertyScriptAssertValidator.class);

        return configuration.addMapping(constraintMapping)
                .buildValidatorFactory()
                .getValidator();
    }
}
