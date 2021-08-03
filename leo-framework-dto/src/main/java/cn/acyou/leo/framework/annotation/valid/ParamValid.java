package cn.acyou.leo.framework.annotation.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用AOP拦截所有controller请求，参数上有@ParamValid注解时，校验此参数
 * {@see cn.com.zhengya.spdapi.aop.ParameterValidateAspect}
 * <p>
 * Examples:
 * <pre>
 *     @PostMapping(value = "validate")
 *     @ResponseBody
 *     public Result<ValidateSo> validate(@ParamValid @RequestBody ValidateSo validateSo) {
 *         return Result.success();
 *     }
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020-3-21 下午 08:39]
 **/
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamValid {

}
