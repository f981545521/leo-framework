package cn.acyou.leo.product.mq;

import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020-9-1 下午 09:29]
 **/
@Slf4j
@Service
@RocketMQMessageListener(
        topic = MQConstant.main_topic,
        consumerGroup = MQConstant.main_topic,
        selectorExpression = "*",//tags
        //selectorExpression = "IN ('TAG1', 'TAG2')",
        consumeMode = ConsumeMode.ORDERLY //顺序消费
        //消息广播 messageModel = MessageModel.BROADCASTING,
        //enableMsgTrace = true, //开启消息轨迹
        //customizedTraceTopic = "trace-topic"
        )
public class MessageConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    /**
     * 只要没有异常出现，那么就会消费成功，有异常出现了就重新进行发送
     */
    @Override
    public void onMessage(String message) {
        log.info("MessageConsumer 收到消息：" + message);
        if (RandomUtil.randomAge() > 90){
            log.info("发生异常");
            throw new ServiceException("unexpect exception.");
        }
        log.info("MessageConsumer 处理成功！");
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {

    }
}
