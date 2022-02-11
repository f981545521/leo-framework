package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.base.DTO;
import cn.acyou.leo.framework.prop.LeoProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import java.lang.reflect.Parameter;
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
    private static final List<String> baseType = Arrays.asList("Long", "Integer", "String", "Boolean", "Double", "Float");

    @Autowired
    private LeoProperty leoProperty;

    /**
     * 所有的Controller
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void pointCutCtl() {

    }

    /**
     * 所有的RestController
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointCutRestCtl() {

    }


    @Before("pointCutCtl() || pointCutRestCtl()")
    public void before(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        final Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        final Parameter[] parameters = methodSignature.getMethod().getParameters();
        Object[] args = jp.getArgs();
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
            final String name = parameters[paramIndex].getName();
            List<Class<? extends Annotation>> annotations = Arrays.stream(parameterAnnotation).map(Annotation::annotationType).collect(Collectors.toList());
            //实体参数校验
            boolean obvious = false;
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestParam_" + name, paramValue);
                    obvious = true;
                }
                if (annotation instanceof RequestBody) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestBody_" + name, paramValue);
                    obvious = true;
                }
                if (annotation instanceof PathVariable) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("PathVariable_" + name, paramValue);
                    obvious = true;
                }
            }
            //不明显的RequestParam类型
            if (!obvious) {
                Object paramValue = args[paramIndex];
                if (baseType.contains(parameterTypes[paramIndex].getSimpleName())) {
                    paramsMap.put("RequestParam_" + name, paramValue);
                }else if (paramValue instanceof DTO) {
                    paramsMap.put("RequestParam_" + name, paramValue.toString());
                }
            }
        }
        AppContext.setRequestParams(paramsMap);
        if (leoProperty.isPrintRequestParam()) {
            log.info("请求地址：{}|参数：{}", AppContext.getActionUrl(), JSON.toJSONString(paramsMap, SerializerFeature.WriteMapNullValue));
        }
    }


}
