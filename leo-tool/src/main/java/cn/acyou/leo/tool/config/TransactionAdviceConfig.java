package cn.acyou.leo.tool.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

/**
 * 通过AOP切面设置全局事务，拦截service包下面指定方法
 *
 * @author youfang
 * @version [1.0.0, 2020-4-19 下午 07:42]
 **/
@Configuration
public class TransactionAdviceConfig {
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* cn.acyou.leo.tool.service..*.*(..))";

    @Autowired
    private TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute txAttr_REQUIRED = new RuleBasedTransactionAttribute();
        txAttr_REQUIRED.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /*  当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务  */
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /*  读已提交  */
        txAttr_REQUIRED.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("insert*", txAttr_REQUIRED);

        source.addTransactionalMethod("del*", txAttr_REQUIRED);
        source.addTransactionalMethod("delete*", txAttr_REQUIRED);

        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("edit*", txAttr_REQUIRED);
        source.addTransactionalMethod("modify*", txAttr_REQUIRED);

        source.addTransactionalMethod("over*", txAttr_REQUIRED);
        source.addTransactionalMethod("cancel*", txAttr_REQUIRED);
        source.addTransactionalMethod("check*", txAttr_REQUIRED);
        source.addTransactionalMethod("commit*", txAttr_REQUIRED);
        source.addTransactionalMethod("finish*", txAttr_REQUIRED);
        source.addTransactionalMethod("active*", txAttr_REQUIRED);
        source.addTransactionalMethod("verify*", txAttr_REQUIRED);
        source.addTransactionalMethod("move*", txAttr_REQUIRED);
        source.addTransactionalMethod("opt*", txAttr_REQUIRED);
        source.addTransactionalMethod("exec*", txAttr_REQUIRED);
        source.addTransactionalMethod("do*", txAttr_REQUIRED);
        source.addTransactionalMethod("set*", txAttr_REQUIRED);

        return new TransactionInterceptor(transactionManager, source);
    }


    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}