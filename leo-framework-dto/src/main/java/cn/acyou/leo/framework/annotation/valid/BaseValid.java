package cn.acyou.leo.framework.annotation.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求参数通用校验类
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface BaseValid {
    //不可为空:(true:不可为空, false:可为空)
    boolean notNull() default false;

    //字符串最大长度:(每个字符/汉字的长度)
    int maxLength() default 0;

    //字符串最小长度:(每个字符/汉字的长度)
    int minLength() default 0;

    //固定长度：固定字符串的长度、数字的长度
    int fixLength() default 0;

    //最小值（包含该值），针对数字类型：（如果<该值，则会报错），如果值为默认值{@link Integer.MIN_VALUE}则不校验
    int min() default Integer.MIN_VALUE;

    //最大值（包含该值），针对数字类型：（如果>该值，则会报错），如果值为默认值{@link Integer.MIN_VALUE}则不校验
    int max() default Integer.MIN_VALUE;

    //取值范围，针对非数字类型
    String[] notInRange() default {};

    //取值范围，针对数字类型
    int[] numberNotInRange() default {};

    //提供几种常用的正则验证
    RegexType regexType() default RegexType.NONE;

    //自定义正则验证
    String regexExpression() default "";

    //参数或者字段描述, 这样能够显示友好的异常信息
    String message() default "";

    //返回错误码信息 暂未使用
    String code() default "";

    //集合类型不能为空
    boolean notEmpty() default false;

    //实体类型 继续校验
    boolean entityValid() default false;

    //实体集合实体 继续校验
    boolean entityCollectionValid() default false;

    //时间类型校验
    DateValidType dateValid() default DateValidType.none;

    //时间类型校验指定日期:   使用类型为if_beforeSpecifyDate,   if_afterSpecifyDate 时，要指定的字段名
    String specifyDateFieldName() default "";

    //校验字典类型取值范围，取值字典编码
    String dictCode() default "";
}
