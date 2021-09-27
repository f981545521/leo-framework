package cn.acyou.leo.framework.aspect;


import cn.acyou.leo.framework.annotation.AccessLimit;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.Md5Util;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(accessLimit)")
    public void pointCut(AccessLimit accessLimit) {

    }

    @Around("pointCut(accessLimit)")
    public Object around(ProceedingJoinPoint joinPoint, AccessLimit accessLimit) throws Throwable {
        long accessInterval = accessLimit.interval();
        boolean includeArgs = accessLimit.includeArgs();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String path = request.getServletPath();
        String token = request.getHeader(Constant.TOKEN_NAME);
        StringBuilder keyBuffer = new StringBuilder(path);
        if (StringUtils.hasText(token)) {
            keyBuffer.append(token);
        }
        Map<Integer, Object> paramsMap = new HashMap<>();
        if (includeArgs) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile) {
                    continue;
                }
                paramsMap.put(i, arg);
            }
            keyBuffer.append("ARGS:");
            keyBuffer.append(Md5Util.md5(JSON.toJSONString(paramsMap)));
        }
        //Redis setNx
        Boolean aBoolean = redisUtils.setIfAbsent(keyBuffer.toString(), "1", accessInterval, TimeUnit.MILLISECONDS);
        if (!aBoolean) {
            return Result.error(CommonErrorEnum.ACCESS_LIMIT);
        }
        return joinPoint.proceed();
    }

}
