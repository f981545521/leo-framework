package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.base.DTO;
import cn.acyou.leo.framework.prop.LeoProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数记录切面
 *
 * @author youfang
 * @version [1.0.0, 2021-09-27 15:28]
 */
@Slf4j
@Aspect
//@Component
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


    @Around("pointCutCtl() || pointCutRestCtl()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        final Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        Object[] args = jp.getArgs();
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
            boolean obvious = false;
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestParam_" + paramIndex, paramValue);
                    obvious = true;
                }
                if (annotation instanceof RequestBody) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("RequestBody_" + paramIndex, paramValue);
                    obvious = true;
                }
                if (annotation instanceof PathVariable) {
                    Object paramValue = args[paramIndex];
                    paramsMap.put("PathVariable_" + paramIndex, paramValue);
                    obvious = true;
                }
            }
            //不明显的RequestParam类型
            if (!obvious) {
                Object paramValue = args[paramIndex];
                if (baseType.contains(parameterTypes[paramIndex].getSimpleName())) {
                    paramsMap.put("RequestParam_" + paramIndex, paramValue);
                }else if (paramValue instanceof DTO) {
                    paramsMap.put("RequestParam_" + paramIndex, paramValue.toString());
                }
            }
        }
        AppContext.setRequestParams(paramsMap);
        if (leoProperty.isPrintRequestBody()) {
            log.info("请求地址：{}|参数：{}", AppContext.getActionUrl(), JSON.toJSONString(paramsMap, SerializerFeature.WriteMapNullValue));
        }
        //执行方法
        Object proceed = jp.proceed();
        log.info("请求结束：{}", JSON.toJSONString(proceed));
        return proceed;
    }


}
