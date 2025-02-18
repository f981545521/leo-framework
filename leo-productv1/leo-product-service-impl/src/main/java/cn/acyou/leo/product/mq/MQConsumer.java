package cn.acyou.leo.product.mq;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.product.dto.bo.ProductAddBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020-9-1 下午 09:29]
 **/
@Slf4j
@Component
public class MQConsumer {

    @Service
    @RocketMQMessageListener(
            topic = MQConstant.product_add,
            consumerGroup = MQConstant.product_add)
    public static class MyConsumer1 implements RocketMQListener<ProductAddBo> {
        @Override
        public void onMessage(ProductAddBo productAddBo) {
            log.info("[MyConsumer1]  收到消息 : {}", productAddBo);
        }
    }

    @Service
    @RocketMQMessageListener(
            topic = MQConstant.product_add_2,
            consumerGroup = MQConstant.product_add_2)
    public static class MyConsumer2 implements RocketMQReplyListener<ProductAddBo, Result<String>> {

        @Override
        public Result<String> onMessage(ProductAddBo message) {
            log.info("[MyConsumer2]  收到消息 : {}", message);
            return Result.success("~~棒棒的~~");
        }
    }

}
