package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.exception.RetryLaterException;
import cn.acyou.leo.framework.util.RetryHelper;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author youfang
 * @version [1.0.0, 2025/7/14 11:18]
 **/
@Slf4j
public class MainTest604 {
    public static void main(String[] args) {
        RetryHelper retry = new RetryHelper(1000, 10000, 1, 10);
        try {
            retry.execute(()->{
                int i = RandomUtil.randomRangeNumber(0, 100);
                log.info("i:{}", i);
                if(i<90){
                    throw new RetryLaterException("数字不对："+i);
                }
                return IdUtil.simpleUUID();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void main2(String[] args) {
        RetryHelper retry = new RetryHelper(1000, 10000, 3, 20);
        try {
            retry.execute(()->{
                int i = RandomUtil.randomRangeNumber(0, 100);
                log.info("i:{}", i);
                if(i<90){
                    throw new RetryLaterException("数字不对："+i);
                }
                return IdUtil.simpleUUID();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
