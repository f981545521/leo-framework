package cn.acyou.leo.framework.annotation.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 集合范围内的值判断
 *
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {

    String message() default "取值范围不正确！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 数字类型
     * @return 数字类型范围
     */
    int[] values() default { };

    /**
     * 字符串类型
     * @return 字符串类型范围
     */
    String[] strValues() default {};
}
