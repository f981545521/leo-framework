package cn.acyou.leo.tool.event;

import cn.acyou.leo.framework.base.LeoEvent;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.tool.dto.dict.DictVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2024/11/26]
 **/
@Slf4j
@Component
public class LeoEventListener {

    @Async
    @EventListener
    public void handleEvent1(LeoEvent<String> event) {
        WorkUtil.trySleep5000();
        log.info("LeoEventListener handleEvent1 收到消息：{}", event);
    }

    @EventListener
    public void handleEvent2(LeoEvent<DictVo> event) {
        WorkUtil.trySleep5000();
        log.info("LeoEventListener handleEvent2 收到消息：{}", event);
    }

}
