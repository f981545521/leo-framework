package cn.acyou.leo.framework.advisor;

import cn.acyou.leo.framework.util.redis.RedisUtils;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2021-11-28]
 **/
@Component
public class RedisLockAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisLockSource redisLockSource;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        RedisLockAnnotationInterceptor interceptor = new RedisLockAnnotationInterceptor(redisUtils, redisLockSource);
        RedisLockPointCut redisLockPointCut = new RedisLockPointCut(redisLockSource);
        this.advisor = new RedisLockAnnotationAdvisor(interceptor, redisLockPointCut);
    }

}