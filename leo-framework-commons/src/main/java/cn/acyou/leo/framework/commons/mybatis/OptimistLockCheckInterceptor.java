package cn.acyou.leo.framework.commons.mybatis;

import cn.acyou.leo.framework.commons.exception.ModifiedByAnotherUserException;
import cn.acyou.leo.framework.commons.exception.RemovedByAnotherUserException;
import cn.acyou.leo.framework.commons.mapper.tkMapper.CommonMapper;
import cn.acyou.leo.framework.commons.util.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 乐观锁检查
 * <p>
 * 1. 实体的属性中要有：
 *          主键注解：{@link Id}
 *          版本号注解：{@link Version}
 * 2. 适用方法：
 *          {@link tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper#updateByPrimaryKey(Object)}
 *          {@link tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper#updateByPrimaryKeySelective(Object)}
 *          {@link CommonMapper#updateByPrimaryKeySelectiveCustom(Object)}
 *          {@link tk.mybatis.mapper.common.base.delete.DeleteMapper#delete(Object)}
 *          在使用这些方法时，当主键和版本号都不为空的时候，检查乐观锁版本
 * <p>
 * 3. 检查乐观锁版本，抛出异常：
 * 被修改：{@link ModifiedByAnotherUserException}
 * 被删除：{@link RemovedByAnotherUserException}
 *
 * @author youfang
 * @version [1.0.0, 2020/7/7]
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class OptimistLockCheckInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(OptimistLockCheckInterceptor.class);

    private static final String SUCCESS_FLAG = "1";
    private static final String ERROR_FLAG = "0";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (SqlCommandType.UPDATE == mappedStatement.getSqlCommandType() || SqlCommandType.DELETE == mappedStatement.getSqlCommandType()) {
            if (invocation.getArgs().length > 1) {
                Object parameterObject = invocation.getArgs()[1];
                Class<?> entityClass = parameterObject.getClass();
                if (entityClass.isAnnotationPresent(Table.class)) {
                    Table annotation = entityClass.getAnnotation(Table.class);
                    String tableName = annotation.name();
                    Field idField = ReflectUtils.recursiveFieldFinder(entityClass, Id.class);
                    Field versionEntityField = ReflectUtils.recursiveFieldFinder(entityClass, Version.class);
                    if (idField != null && versionEntityField != null) {
                        String idColumnName = idField.getAnnotation(Column.class).name();
                        String versionColumnName = versionEntityField.getAnnotation(Column.class).name();
                        Object idColumnValue = ReflectUtils.getFieldValue(parameterObject, idField);
                        Object versionColumnValue = ReflectUtils.getFieldValue(parameterObject, versionEntityField);
                        if (!StringUtils.EMPTY.equals(idColumnName) && !StringUtils.EMPTY.equals(versionColumnName) && idColumnValue != null && versionColumnValue != null) {
                            Executor executor = (Executor) invocation.getTarget();
                            Connection connection = executor.getTransaction().getConnection();
                            Object databaseVersionCheck = null;
                            //select version = 35 from table WHERE id = 1;  result -  正确：1，错误：0，不存在：null
                            PreparedStatement stmt = connection.prepareStatement(String.format("SELECT %s = ? FROM %s WHERE %s = ?", versionColumnName, tableName, idColumnName));
                            stmt.setObject(1, versionColumnValue);
                            stmt.setObject(2, idColumnValue);
                            log.info(stmt.toString());
                            stmt.execute();
                            ResultSet rs = stmt.getResultSet();
                            if (rs.first()) {
                                databaseVersionCheck = rs.getObject(1);
                                rs.close();
                            }
                            stmt.close();
                            //adjust
                            if (databaseVersionCheck == null) {
                                throw new RemovedByAnotherUserException();
                            }
                            if (ERROR_FLAG.equals(databaseVersionCheck.toString())) {
                                throw new ModifiedByAnotherUserException();
                            }
                            if (SqlCommandType.UPDATE == mappedStatement.getSqlCommandType()) {
                                Integer version = Integer.parseInt(versionColumnValue.toString()) + 1;
                                ReflectUtils.setFieldValue(versionColumnName, parameterObject, version);
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
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
