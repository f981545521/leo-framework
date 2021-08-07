package cn.acyou.leo.framework.mybatis;

import cn.acyou.leo.framework.base.BaseEntity;
import cn.acyou.leo.framework.context.StaticInfo;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/7]
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class BaseEntityInterceptor implements Interceptor {
    private static final String COLLECTION_KEY = "collection";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (invocation.getArgs().length > 1) {
            Object parameterObject = invocation.getArgs()[1];
            if (parameterObject instanceof Map) {
                Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                if (paramMap.containsKey(COLLECTION_KEY)) {
                    Collection<?> collection = (Collection<?>) paramMap.get(COLLECTION_KEY);
                    for (Object obj : collection) {
                        handlerBaseField(mappedStatement, obj);
                    }
                }
            } else {
                handlerBaseField(mappedStatement, parameterObject);
            }
        }
        return invocation.proceed();
    }

    /**
     * 处理应用中的基本字段
     *
     * @param mappedStatement 映射语句
     * @param parameterObject 参数对象
     */
    private void handlerBaseField(MappedStatement mappedStatement, Object parameterObject) {
        if (parameterObject instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) parameterObject;
            if (SqlCommandType.INSERT == mappedStatement.getSqlCommandType()) {
                if (baseEntity.getCreateUser() == null) {
                    baseEntity.setCreateUser(StaticInfo.getUserId());
                }
                if (baseEntity.getCreateTime() == null) {
                    baseEntity.setCreateTime(new Date());
                }
                if (baseEntity.getUpdateUser() == null) {
                    baseEntity.setUpdateUser(baseEntity.getCreateUser());
                }
                if (baseEntity.getUpdateTime() == null) {
                    baseEntity.setUpdateTime(new Date());
                }
            }
            if (SqlCommandType.UPDATE == mappedStatement.getSqlCommandType()) {
                if (baseEntity.getUpdateUser() == null) {
                    baseEntity.setUpdateUser(StaticInfo.getUserId());
                }
                if (baseEntity.getUpdateTime() == null) {
                    baseEntity.setUpdateTime(new Date());
                }
            }
        }

    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
