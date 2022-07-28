package cn.acyou.leo.tool.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 */
@Slf4j
@Component
public class AutoTask {

    @Scheduled(cron = "0 0/5 * * * ?")
    public void autoAudit() {
        log.info("发起机器审核... ");
    }
}
