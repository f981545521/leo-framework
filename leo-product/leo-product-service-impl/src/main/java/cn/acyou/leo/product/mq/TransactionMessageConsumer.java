package cn.acyou.leo.product.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020/9/9]
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = "transaction_topic", consumerGroup = "transaction-topic_consumer_group")
public class TransactionMessageConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.printf("------- StringTransactionalConsumer received: %s \n", message);
    }
}
