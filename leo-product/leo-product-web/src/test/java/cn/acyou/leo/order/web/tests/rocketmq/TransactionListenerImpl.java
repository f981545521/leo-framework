package cn.acyou.leo.order.web.tests.rocketmq;

import cn.acyou.leo.framework.util.RandomUtil;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youfang
 * @version [1.0.0, 2020/9/9]
 **/
public class TransactionListenerImpl implements TransactionListener {
    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //随机的状态
        int status = RandomUtil.randomFromPool(0, 1, 2);

        localTrans.put(msg.getTransactionId(), status);
        if (status == 0) {
            System.out.println("# COMMIT    执行本地交易，status:" + status);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        if (status == 1) {
            System.out.println("# ROLLBACK    执行本地交易，status:" + status);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        System.out.println("# UNKNOW    执行本地交易，status:" + status);
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        //RocketMQ 状态回查
        Integer status = localTrans.get(msg.getTransactionId());
        System.out.println("检查本地交易 -> " + msg.getMsgId() + "status:" + status);
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
