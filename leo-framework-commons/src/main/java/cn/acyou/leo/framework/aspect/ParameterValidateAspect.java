package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.annotation.valid.BaseValid;
import cn.acyou.leo.framework.annotation.valid.EnhanceValid;
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
import java.util.*;
import java.util.stream.Collectors;


/**
 * 参数验证切面
 *
 * @author youfang
 * @date 2020/12/04
 */
@Aspect
@Component
public class ParameterValidateAspect {

    private static final List<Class<? extends Annotation>> PARAM_VALID_BASE = Arrays.asList(ParamValid.class, BaseValid.class);
    private static final List<Class<? extends Annotation>> PARAM_VALID_ENHANCE = Arrays.asList(ParamValid.class, EnhanceValid.class);

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
            List<Class<? extends Annotation>> annotations = Arrays.stream(parameterAnnotation).map(Annotation::annotationType).collect(Collectors.toList());
            // ParamValid + BaseValid 组合支持
            boolean singleValid = annotations.containsAll(PARAM_VALID_BASE);
            if (singleValid){
                Object validValue = args[paramIndex];
                paramsMap.put("Arg_" + paramIndex, validValue);
                int i = annotations.indexOf(BaseValid.class);
                EnhanceValidUtil.validBaseType(validValue, "Arg" + paramIndex, (BaseValid) parameterAnnotation[i], null);
                continue;
            }
            // ParamValid + EnhanceValid 组合支持
            boolean singleEnhanceValid = annotations.containsAll(PARAM_VALID_ENHANCE);
            if (singleEnhanceValid){
                Object validValue = args[paramIndex];
                paramsMap.put("Arg_" + paramIndex, validValue);
                int i = annotations.indexOf(EnhanceValid.class);
                EnhanceValid enhanceValid = (EnhanceValid) parameterAnnotation[i];
                for (BaseValid baseValid : enhanceValid.value()) {
                    EnhanceValidUtil.validBaseType(validValue, "Arg" + paramIndex, baseValid, null);
                }
                continue;
            }
            //实体参数校验
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
