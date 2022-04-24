package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.annotation.AutoIdempotent;
import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.redis.RedisUtils;
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

import javax.servlet.http.HttpServletRequest;

/**
 * 防止重复提交切面
 *
 * @author hzh
 */
@Slf4j
@Aspect
@Component
public class AutoIdempotentAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(autoIdempotent)")
    public void pointCut(AutoIdempotent autoIdempotent) {

    }

    @Around("pointCut(autoIdempotent)")
    public Object around(ProceedingJoinPoint joinPoint, AutoIdempotent autoIdempotent) throws Throwable {
        String prefix = autoIdempotent.prefix();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String sequence = request.getParameter(Constant.AUTO_IDEMPOTENT_SEQUENCE);
        if (!StringUtils.hasText(sequence)) {
            throw new ServiceException("请先获取序列号！");
        }
        String key = RedisKeyConstant.AUTO_IDEMPOTENT_SEQUENCE + prefix + sequence;
        String v = null;
        try {
            v = redisUtils.get(key);
            if (!StringUtils.hasText(v)) {
                throw new ServiceException(CommonErrorEnum.REPETITIVE_OPERATION);
            }
            return joinPoint.proceed();
        } finally {
            //使用过就删除序列
            redisUtils.delete(key);
        }
    }

}
