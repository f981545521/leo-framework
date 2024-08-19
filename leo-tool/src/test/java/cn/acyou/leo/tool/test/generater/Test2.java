package cn.acyou.leo.tool.test.generater;

import cn.acyou.leo.framework.util.DateUtil;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2022-5-24]
 **/
public class Test2 {
    public static void main(String[] args) {
        String cronExpression = "0 5/60 0/1 * * ?";
        CronExpression parse = CronExpression.parse(cronExpression);
        LocalDateTime next = LocalDateTime.now();
        for (int i = 0; i < 50; i++) {
            next = parse.next(next);
            System.out.println(next.format(DateTimeFormatter.ofPattern(DateUtil.FORMAT_DATE_TIME)));
        }
    }


    public static void main1(String[] args) {
        final CronTrigger cronTrigger = new CronTrigger("0 0/10 * * * ? ");
        System.out.println("下一次执行时间：" + DateUtil.getDateFormat(cronTrigger.nextExecutionTime(new SimpleTriggerContext())));
    }

    public static void main2(String[] args) {
        String cronExpression = "0 5 0/1 * * ?";
        CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
        // 获取下一次执行时间
        Date nextExecutionTime = new Date();
        for (int i = 0; i < 50; i++) {
            nextExecutionTime = generator.next(nextExecutionTime);
            System.out.println("Next execution time: " + DateUtil.getDateFormat(nextExecutionTime));
        }
    }

}
