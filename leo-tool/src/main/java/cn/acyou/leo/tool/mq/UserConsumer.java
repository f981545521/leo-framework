package cn.acyou.leo.tool.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2025/4/3 15:22]
 **/
@Slf4j
@Component
public class UserConsumer {

    @KafkaListener(topics = "user_log", groupId = "user_group", containerFactory = "kafkaListenerContainerFactory")
    public void handleUserLog(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        String value = record.value();
        log.info("UserConsumer消息 user_group [topic]={}, [partition]={}, [offset]={}, [value]={}", record.topic(), record.partition(), record.offset(), value);
        if ("error".equals(key)) {
            throw new RuntimeException("error");
        }
        ack.acknowledge();
    }

    @KafkaListener(topics = "user_log_history", groupId = "user_group_db", containerFactory = "kafkaListenerContainerFactory")
    public void handleHistroy(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info("UserConsumer消息 user_group_db [topic]={}, [partition]={}, [offset]={}, [value]={}", record.topic(), record.partition(), record.offset(), value);
    }

    @KafkaListener(topics = "user_log_history", groupId = "user_group_redis", containerFactory = "kafkaListenerContainerFactory")
    public void handleHistroyRedis(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info("UserConsumer消息 user_group_redis [topic]={}, [partition]={}, [offset]={}, [value]={}", record.topic(), record.partition(), record.offset(), value);
    }

}
