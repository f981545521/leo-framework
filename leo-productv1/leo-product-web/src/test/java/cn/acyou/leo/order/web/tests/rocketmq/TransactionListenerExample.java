package cn.acyou.leo.order.web.tests.rocketmq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author youfang
 * @version [1.0.0, 2020/9/9]
 **/
public class TransactionListenerExample implements TransactionListener {

    /** 预准备消息成功 */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        //方案1：在预准备消息中执行业务逻辑
        //思考： 这样处理会不会出现 本地事务与MQ消息不一致的情况？
        //1. 本地方法发生异常后直接回滚，没有@Transaction能回滚吗？
        //2. 本地事务提交后，COMMIT_MESSAGE一定能发出去吗？
        //3. 确认消息COMMIT_MESSGE发出，但因网络不可达RocketMQ集群没收到。
        //4.
        try {
            //执行业务逻辑
        }catch (Exception e){
            e.printStackTrace();
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;


        //方案2：executeLocalTransaction统一返回UNKNOWN，由checkLocalTransaction中判断本地事务状态。
        //需要一个事务提交记录表，事务提交成功后，插入一条记录到表中
        //checkLocalTransaction去查这个表，有记录返回COMMIT，如果没有记录：
        //有可能方法正在执行，所以消息中包含消息创建时间。
        //1. 如果消息的时间在10MIN 以内，没有记录，返回UNKNOWN
        //2. 如果消息的时间在10MIN 之外，没有记录，返回ROLLBACK

        //return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        //RocketMQ 状态回查 直接丢弃
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
