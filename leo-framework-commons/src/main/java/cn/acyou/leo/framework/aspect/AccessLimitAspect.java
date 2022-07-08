package cn.acyou.leo.framework.aspect;


import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.ElParser;
import cn.acyou.leo.framework.util.Md5Util;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交切面
 *
 * use support {@link AccessLimit}
 *
 * @author fangyou
 * @version [1.0.0, 2021-09-26 18:01]
 */
@Slf4j
@Aspect
@Component
public class AccessLimitAspect {
    //标识 prefix
    private static final String ACCESS_LIMIT_KEY_PREFIX = "ACCESS_LIMIT:";

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(accessLimit)")
    public void pointCut(AccessLimit accessLimit) {

    }

    @Around("pointCut(accessLimit)")
    public Object around(ProceedingJoinPoint joinPoint, AccessLimit accessLimit) throws Throwable {
        String keyValue = getKeyValue(joinPoint, accessLimit.value());
        long accessInterval = accessLimit.interval();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String path = request.getServletPath();
        String token = request.getHeader(Constant.TOKEN_NAME);
        StringBuilder keyBuffer = new StringBuilder(path);
        if (StringUtils.hasText(token)) {
            keyBuffer.append(token);
        }
        keyBuffer.append("ARGS:");
        keyBuffer.append(Md5Util.md5(keyValue));
        if (accessInterval == -1) {
            //设置 1 小时 ：3600000
            accessInterval = 60 * 60 * 1000;
        }
        //Redis setNx
        Boolean aBoolean = redisUtils.setIfAbsent(keyBuffer.toString(), "1", accessInterval, TimeUnit.MILLISECONDS);
        if (!aBoolean) {
            log.error("接口访问限制 |访问最低间隔：{}", accessLimit.interval());
            if (accessLimit.interval() == -1) {
                return Result.error(CommonErrorEnum.CONCURRENT_ERROR);
            } else {
                return Result.error(CommonErrorEnum.ACCESS_LIMIT);
            }
        }
        try {
            return joinPoint.proceed();
        } finally {
            redisUtils.delete(keyBuffer.toString());
        }
    }

    public String getKeyValue(ProceedingJoinPoint joinPoint , String express) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        return ACCESS_LIMIT_KEY_PREFIX + signature.getDeclaringTypeName() + ":" + methodName + ":" + ElParser.getKey(express, parameterNames, joinPoint.getArgs());
    }

}
