package cn.acyou.leo.framework.annotation.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字典值判断
 *
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface DictValue {

    String message() default "字典值不正确！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 字典code
     * @return 字典code
     */
    String dictCode() default "";

}
