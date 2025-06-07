package cn.acyou.leo.tool.config;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableKafka
//@EnableTransactionManagement
public class KafkaConfig {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> retryListenerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory.getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties())));
        // 配置重试模板
        factory.setRetryTemplate(retryTemplate());
        // 设置重试完成后的恢复回调
        factory.setRecoveryCallback(context -> {
            ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) context.getAttribute("record");
            Acknowledgment ack = (Acknowledgment) context.getAttribute("acknowledgment");
            // 记录重试失败信息
            final Throwable rootCause = Throwables.getRootCause(context.getLastThrowable());
            log.error("最终消息处理失败 topic: " + record.topic() + "，msg:" + record.value() + ", exception: " + rootCause.getMessage(), rootCause);

            //1.  可以将消息发送到死信主题
            kafkaTemplate.send("retry-failed-topic", record.value());
            //2.  发送飞书消息来手动处理

            // 手动确认消息，防止重复消费
            if (ack != null) {
                ack.acknowledge();
            }
            return null;
        });
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> batchListenerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    //@Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        //factory.setErrorHandler((e, consumerRecord) -> {
        //    log.error("当前消息处理失败, value: {}, exception: {}", consumerRecord.value(), e.getMessage());
        //});

        // 设置手动提交偏移量
        //factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // 配置重试模板
        factory.setRetryTemplate(retryTemplate());
        // 设置重试完成后的恢复回调
        factory.setRecoveryCallback(context -> {
            ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) context.getAttribute("record");
            Acknowledgment ack = (Acknowledgment) context.getAttribute("acknowledgment");
            // 记录重试失败信息
            final Throwable rootCause = Throwables.getRootCause(context.getLastThrowable());
            log.error("最终消息处理失败 topic: " + record.topic() + "，msg:" + record.value() + ", exception: " + rootCause.getMessage(), rootCause);

            // 可以将消息发送到死信主题
            kafkaTemplate.send("retry-failed-topic", record.value());
            // 手动确认消息，防止重复消费
            if (ack != null) {
                ack.acknowledge();
            }
            return null;
        });

        return factory;
    }



    // 配置重试模板
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();
        // 配置重试策略：最大尝试次数为3次
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        template.setRetryPolicy(retryPolicy);
        // 配置退避策略：指数退避，初始1秒，最大30秒
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(5000); // 初始间隔1秒
        backOffPolicy.setMultiplier(3.0); // 倍数，每次间隔时间翻倍
        backOffPolicy.setMaxInterval(5*60000); // 最大间隔
        template.setBackOffPolicy(backOffPolicy);
        return template;
    }

}
