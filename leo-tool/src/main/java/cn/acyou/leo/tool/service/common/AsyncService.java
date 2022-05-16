package cn.acyou.leo.tool.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/16 11:52]
 **/
@Slf4j
@Service
public class AsyncService {

    @Async
    public void printOk() {
        log.info("ok");
    }
}
