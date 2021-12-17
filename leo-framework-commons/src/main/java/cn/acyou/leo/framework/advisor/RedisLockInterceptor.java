package cn.acyou.leo.framework.advisor;

import cn.acyou.leo.framework.exception.ConcurrentException;
import cn.acyou.leo.framework.util.ElParser;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;

/**
 * @author youfang
 * @version [1.0.0, 2021-11-28]
 **/
@Slf4j
public class RedisLockInterceptor implements MethodInterceptor, Ordered {

    //标识 prefix
    private static final String REDIS_LOCK_KEY_PREFIX = "RedisAnn_LOCK:";

    private final RedisUtils redisUtils;

    private final RedisLockSource redisLockSource;


    public RedisLockInterceptor(RedisUtils redisUtils, RedisLockSource redisLockSource) {
        this.redisUtils = redisUtils;
        this.redisLockSource = redisLockSource;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        RedisLockSource.RedisLockMetadata metadata = redisLockSource.getRedisLockMetadata(invocation.getMethod(), targetClass);
        if (metadata != null) {
            Object result;
            String lockId = null;
            String keyValue = getKeyValue(invocation, targetClass, metadata.getKey());
            try {
                int waitTime = metadata.getWaitTime();
                if (waitTime > 0) {
                    lockId = redisUtils.lockLoop(keyValue, waitTime, metadata.getExpire());
                } else {
                    lockId = redisUtils.lock(keyValue, metadata.getExpire());
                }
                log.info("RedisLock Exec ==> lockId:{}", lockId);
                if (lockId == null) {
                    throw new ConcurrentException();
                }
                result = invocation.proceed();
            } finally {
                redisUtils.unLock(keyValue, lockId);
            }
            return result;
        }
        return invocation.proceed();
    }

    public String getKeyValue(MethodInvocation invocation, Class<?> targetClass, String express) {
        //获取到参数名称
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(invocation.getMethod());
        String methodName = invocation.getMethod().getName();
        String className = "";
        if (targetClass != null) {
            className = targetClass.getName() + ":";
        }
        return REDIS_LOCK_KEY_PREFIX + className + methodName + ":" + ElParser.getKey(express, parameterNames, invocation.getArguments());
    }
}