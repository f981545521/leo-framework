package cn.acyou.leo.tool.service.common;

import cn.acyou.leo.framework.advisor.RedisLock;
import cn.acyou.leo.framework.exception.RetryLaterException;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.tool.dto.dict.DictVo;
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

    /**
     * service层处理并发事务加锁可能会无效
     * 由于spring事务是通过AOP实现的，所以在startSeckillLock()方法执行之前会开启事务，之后会有提交事务的逻辑。而lock的动作是发生在事务之内。
     * 数据库默认的事务隔离级别为可重复读（repeatable-read）。因为是事务先开启后加锁，
     * 隔离级别为可重复读的情况下，当前线程是读取不到其他线程更新的数据，
     * 也就是说其他线程虽然更新了库存且事务也提交了，但是因为当前线程已经开启了事务（可重复读的隔离级别）
     */

    //使用synchronized + 对象 来实现锁不同KEY
    private Object KEY = "";

    public void testSynchronized(DictVo dictVo) {
        KEY = dictVo.getName();
        synchronized (KEY) {
            log.info("testSynchronized start...");
            WorkUtil.trySleep(3000);
            log.info("testSynchronized end.");
        }
    }

    @RedisLock(key = "#name", waitTime = 10000)
    public void testSynchronized2(String name) {
        log.info("testSynchronized start...");
        WorkUtil.trySleep(3000);
        log.info("testSynchronized end.");
    }


    /**
     * 2022-08-09 14:37:08.532  INFO d392dfc6d8d44f439eff5f23e3d4c8e8 [nio-8076-exec-1] c.a.l.f.interceptor.BaseInterceptor      :108  访问开始 ——> 192.168.4.65 [GET /leo-tool/test/testSynchronized?name=AAAB]
     * 2022-08-09 14:37:08.565  INFO d392dfc6d8d44f439eff5f23e3d4c8e8 [nio-8076-exec-1] c.a.l.tool.service.common.CommonService  :32   testSynchronized。。。
     * 2022-08-09 14:37:09.622  INFO 78b2349fde8340f7ad9d8e5ee5cf61cc [nio-8076-exec-8] c.a.l.f.interceptor.BaseInterceptor      :108  访问开始 ——> 192.168.4.65 [GET /leo-tool/test/testSynchronized?name=AAA]
     * 2022-08-09 14:37:11.566  INFO d392dfc6d8d44f439eff5f23e3d4c8e8 [nio-8076-exec-1] c.a.l.tool.service.common.CommonService  :34   testSynchronized end。。。
     * 2022-08-09 14:37:11.566  INFO 78b2349fde8340f7ad9d8e5ee5cf61cc [nio-8076-exec-8] c.a.l.tool.service.common.CommonService  :32   testSynchronized。。。
     * 2022-08-09 14:37:11.612  INFO d392dfc6d8d44f439eff5f23e3d4c8e8 [nio-8076-exec-1] c.a.l.f.interceptor.BaseInterceptor      :203  访问结束 <——  [status:200 耗时:3081 ms]
     * 2022-08-09 14:37:14.580  INFO 78b2349fde8340f7ad9d8e5ee5cf61cc [nio-8076-exec-8] c.a.l.tool.service.common.CommonService  :34   testSynchronized end。。。
     * 2022-08-09 14:37:14.581  INFO 78b2349fde8340f7ad9d8e5ee5cf61cc [nio-8076-exec-8] c.a.l.f.interceptor.BaseInterceptor      :203  访问结束 <——  [status:200 耗时:4959 ms]
     * 结论：同步方法调用会按照顺序执行
     * @param dictVo
     */
    //public synchronized void testSynchronized(DictVo dictVo){
    //    log.info("testSynchronized start...");
    //    WorkUtil.trySleep(3000);
    //    log.info("testSynchronized end.");
    //}
}
