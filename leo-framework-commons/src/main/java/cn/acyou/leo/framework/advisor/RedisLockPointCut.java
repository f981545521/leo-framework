package cn.acyou.leo.framework.advisor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * @author youfang
 * @version [1.0.0, 2021-11-28]
 **/
public class RedisLockPointCut implements Pointcut, MethodMatcher {

    private final RedisLockSource redisLockSource;

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        RedisLockSource.RedisLockMetadata redisLockMetadata = redisLockSource.getRedisLockMetadata(method, targetClass);
        return redisLockMetadata != null;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }

    public RedisLockPointCut(RedisLockSource redisLockSource) {
        this.redisLockSource = redisLockSource;
    }
}