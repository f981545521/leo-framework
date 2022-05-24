package cn.acyou.leo.tool.test.generater;

import cn.acyou.leo.framework.util.DateUtil;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

/**
 * @author youfang
 * @version [1.0.0, 2022-5-24]
 **/
public class Test2 {
    public static void main(String[] args) {
        final CronTrigger cronTrigger = new CronTrigger("0 0/10 * * * ? ");
        System.out.println("下一次执行时间：" + DateUtil.getDateFormat(cronTrigger.nextExecutionTime(new SimpleTriggerContext())));
    }
}
