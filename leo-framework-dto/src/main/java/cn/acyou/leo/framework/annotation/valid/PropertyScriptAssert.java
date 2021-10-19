package cn.acyou.leo.framework.annotation.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface PropertyScriptAssert {
    String message() default "参数不符合规范！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String script();

    @Target({ TYPE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        PropertyScriptAssert[] value();
    }

}
