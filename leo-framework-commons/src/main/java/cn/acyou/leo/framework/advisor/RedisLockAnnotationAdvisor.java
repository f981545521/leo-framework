package cn.acyou.leo.framework.advisor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.lang.NonNullApi;

import javax.validation.constraints.NotNull;

/**
 * @author youfang
 * @version [1.0.0, 2021-11-28]
 **/
public class RedisLockAnnotationAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;
    private final Pointcut pointcut;


    public RedisLockAnnotationAdvisor(Advice advice, Pointcut pointcut) {
        this.advice = advice;
        this.pointcut = pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public boolean isPerInstance() {
        return false;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
