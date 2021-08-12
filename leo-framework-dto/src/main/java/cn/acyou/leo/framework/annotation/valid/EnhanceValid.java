package cn.acyou.leo.framework.annotation.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 增强实体参数校验
 * 加载请求参数中
 * 使用示例：
 *
 * <pre>
 *      #EnhanceValid({
 *         #BaseValid(notNull = true, message = "姓名不能为空"),
 *         #BaseValid(maxLength = 10, message = "姓名过长"),
 *         #BaseValid(minLength = 2, message = "姓名过短"),
 *         #BaseValid(range = {"张三", "李四"}, message = "姓名不在范围内")
 *     })
 *     private String name;
 * </pre>
 * 在方法中使用：
 * <pre>
 *     EnhanceValidUtil.valid(validateSo);
 *  </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface EnhanceValid {

    BaseValid[] value();

}
