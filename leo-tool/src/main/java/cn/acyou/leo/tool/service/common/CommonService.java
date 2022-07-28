package cn.acyou.leo.tool.service.common;

import cn.acyou.leo.framework.exception.RetryLaterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/28 11:18]
 **/
@Slf4j
@Service
public class CommonService {

    @Retryable(value = RetryLaterException.class,   //需要进行重试的异常，和参数includes是一个意思。默认为空，当参数exclude也为空时，所有异常都将要求重试。
            maxAttempts = 2,                        //重试2次
            backoff = @Backoff(value = 3000L)       //重试间隔（3秒）
    )//参数详解：https://www.jianshu.com/p/702fd5f3adf2
    public void testRetry(String key) {
        log.info("输入KEY：{}", key);
        if ("retry".equals(key)) {
            throw new RetryLaterException();
        }
        log.info("运行结束");
    }
}
