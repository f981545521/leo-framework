package cn.acyou.leo.tool.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

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

    @Async
    public Future<String> getResult() {
        log.info("ok");
        return new AsyncResult<>("ok");
    }

    @Async
    public void exec(String name) {
        log.info("开始执行");
        log.info(name);
        if ("Exception".equals(name)) {
            int i = 1 / 0;
        }
        log.info("执行结束");
    }
}
