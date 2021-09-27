package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.annotation.valid.ParamValid;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.valid.EnhanceValidUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 参数验证切面
 *
 * @author youfang
 * @date 2020/12/04
 */
@Aspect
@Component
public class ParameterValidateAspect {

    /**
     * 切入所有Controller 的请求
     */
    @Pointcut("execution(* cn.acyou.leo.*.controller.*..*(..))")
    public void parameterValid() {

    }

    @Before("parameterValid()")
    public void before(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] args = jp.getArgs();
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof ParamValid) {
                    Object paramValue = args[paramIndex];
                    EnhanceValidUtil.valid(paramValue);
                }
                if (annotation instanceof RequestParam) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestParam_" + paramIndex, paramValue);
                }
                if (annotation instanceof RequestBody) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestBody_" + paramIndex, paramValue);
                }
                if (annotation instanceof PathVariable) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("PathVariable_" + paramIndex, paramValue);
                }
            }
        }
        AppContext.setRequestParams(paramsMap);
    }


}
