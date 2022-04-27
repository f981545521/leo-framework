package cn.acyou.leo.tool.task;

import cn.acyou.leo.tool.task.base.AbstractTaskParent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/27 14:19]
 **/
@Slf4j
@Component
public class MyDynamicTask extends AbstractTaskParent {
    @Override
    public void run(String params) {
        log.info("执行了MyDynamicTask...");
    }
}
