package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.prop.LeoProperty;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 参数记录切面
 *
 * @author youfang
 * @version [1.0.0, 2021-09-27 15:28]
 */
@Slf4j
@Aspect
@Component
public class ParameterRecordAspect {
    @Autowired
    private LeoProperty leoProperty;

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
            //实体参数校验
            for (Annotation annotation : parameterAnnotation) {
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
        if (leoProperty.isPrintRequestParam()) {
            log.info("请求地址：{}|参数：{}", AppContext.getActionUrl(), JSON.toJSONString(paramsMap));
        }
    }


}
