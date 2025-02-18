package cn.acyou.leo.order.web.tests.rocketmq;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.order.web.base.ApplicationBaseTests;
import cn.acyou.leo.product.dto.bo.ProductAddBo;
import cn.acyou.leo.product.mq.MQConstant;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;

/**
 * 官方示例：
 * https://github.com/apache/rocketmq-spring/blob/master/rocketmq-spring-boot-samples/rocketmq-produce-demo/src/main/java/org/apache/rocketmq/samples/springboot/ProducerApplication.java
 * @author youfang
 * @version [1.0.0, 2021-8-6]
 **/
public class RocketTemplateTests extends ApplicationBaseTests {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void test1() {
        ProductAddBo productAddBo = new ProductAddBo();
        productAddBo.setProductName("风扇");
        productAddBo.setPrice(new BigDecimal("200.00"));
        SendResult sendResult = rocketMQTemplate.syncSend(MQConstant.product_add, productAddBo);
        System.out.println(sendResult);
        Message<ProductAddBo> build = MessageBuilder
                .withPayload(productAddBo)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
        SendResult sendResult1 = rocketMQTemplate.syncSend(MQConstant.product_add, build);
        System.out.println(sendResult1);
    }

    @Test
    public void test2() {
        // 异步发送request并且等待User类型的返回值
        rocketMQTemplate.sendAndReceive(MQConstant.product_add_2
                , new ProductAddBo("手机", new BigDecimal("12.00"))
                , new RocketMQLocalRequestCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> message) {
                System.out.printf("send user object and receive %s %n", message.toString());
            }

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
            }
        }, 5000);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test3(){
        //只能携带一个标签
        rocketMQTemplate.convertAndSend(MQConstant.main_topic + ":tag0", "I'm from tag0");  // tag0 will not be consumer-selected
    }

}
