package cn.acyou.leo.product.web.controller;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.product.entity.Student;
import cn.acyou.leo.product.mq.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 *
 * @author youfang
 * @version [1.0.0, 2020-9-1 下午 09:10]
 **/
@Slf4j
@Controller
@RequestMapping("mq")
public class RocketMQController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/test1")
    @ResponseBody
    public Result<?> test1(String message) {
        log.info("RocketMQ 准备发送：" + message);
        //同步发送消息
        rocketMQTemplate.convertAndSend(MQConstant.main_topic, "Hello, World!");
        //同步发送消息2
        rocketMQTemplate.send(MQConstant.main_topic, MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        //同步发送消息3
        SendResult sendResult = rocketMQTemplate.syncSend(MQConstant.main_topic, Student.builder().age(18).name("Kitty").build());
        System.out.printf("同步发送消息3 to topic %s sendResult=%s %n", MQConstant.main_topic, sendResult);
        //同步发送消息4
        sendResult = rocketMQTemplate.syncSend(MQConstant.main_topic, MessageBuilder.withPayload(
                Student.builder().age(18).name("Kitty").build()).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build());
        System.out.printf("同步发送消息4 to topic %s sendResult=%s %n", MQConstant.main_topic, sendResult);

        //异步发送消息
        rocketMQTemplate.asyncSend(MQConstant.main_topic, new Student(1, "张飞", 34, null), new SendCallback() {
            @Override
            public void onSuccess(SendResult var1) {
                System.out.printf("异步发送消息 onSucess SendResult=%s %n", var1);
            }

            @Override
            public void onException(Throwable var1) {
                System.out.printf("异步发送消息 onException Throwable=%s %n", var1);
            }

        });
        //发送顺序消息
        rocketMQTemplate.syncSendOrderly(MQConstant.main_topic, MessageBuilder.withPayload("Hello, World").build(),"hashkey");
        //发送延时消息
        rocketMQTemplate.asyncSend(MQConstant.main_topic, MessageBuilder.withPayload(message).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult var1) {
                System.out.printf("async onSucess SendResult=%s %n", var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
                System.out.printf("async onException Throwable=%s %n", var1);
            }
        }, 3000, 3);

        //发送消息并且收到消息
        //String replyGenericObject = rocketMQTemplate.sendAndReceive(MQConstant.main_topic, "request generic",
        //        new TypeReference<String>() {
        //        }.getType(), 30000, 2);
        //System.out.printf("send %s and receive %s %n", "request generic", replyGenericObject);
        return Result.success();
    }

}
