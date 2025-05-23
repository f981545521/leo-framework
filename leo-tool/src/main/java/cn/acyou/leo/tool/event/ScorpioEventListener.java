package cn.acyou.leo.tool.event;

import cn.acyou.leo.framework.model.LeoEvent;
import cn.acyou.leo.tool.dto.dict.DictVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2024/11/26]
 **/
@Slf4j
@Component
public class ScorpioEventListener {

    @EventListener
    public void handleEvent1(LeoEvent<String> event) {
        log.info("ScorpioEventListener handleEvent1 收到消息：{}", event);
    }

    @EventListener
    public void handleEvent2(LeoEvent<DictVo> event) {
        log.info("ScorpioEventListener handleEvent1 收到消息：{}", event);
    }

}
