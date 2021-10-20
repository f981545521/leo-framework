package cn.acyou.leo.framework.mybatis;

import cn.acyou.leo.framework.annotation.mapper.Desensitized;
import cn.acyou.leo.framework.annotation.mapper.International;
import cn.acyou.leo.framework.annotation.mapper.SensitizedType;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.util.DesensitizedUtil;
import cn.acyou.leo.framework.util.ReflectUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 国际化/脱敏 查询结果拦截器
 *
 * 示例：
 * <pre>
 *  *      @Column(name = "name")
 *  *      @International
 *  *      private String name;
 * </pre>
 *
 *     通过加在mybatis返回的实体上的注解，解析注解，取
 *     指定分隔符{@link International}
 *     第几个{@link cn.acyou.leo.framework.base.ClientLanguage }分割串
 * @author youfang
 * @version [1.0.0, 2021/10/15]
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class QueryResultInterceptor implements Interceptor {
    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object returnValue = invocation.proceed();
        if (returnValue instanceof List) {
            List<Object> returnValueList = (List) returnValue;
            if (returnValueList.size() == 0) {
                return returnValue;
            }
            Class<?> resClass = returnValueList.get(0).getClass();
            Field[] declaredFields = resClass.getDeclaredFields();
            Map<String, String> internationalFieldMap = new HashMap<>();
            Map<String, SensitizedType> desensitizedFieldMap = new HashMap<>();
            for (Field declaredField : declaredFields) {
                International annotation = declaredField.getAnnotation(International.class);
                if (annotation != null) {
                    internationalFieldMap.put(declaredField.getName(), annotation.separator());
                }
                Desensitized desensitizedAnnotation = declaredField.getAnnotation(Desensitized.class);
                if (desensitizedAnnotation != null) {
                    desensitizedFieldMap.put(declaredField.getName(), desensitizedAnnotation.type());
                }
            }
            if (internationalFieldMap.isEmpty() && desensitizedFieldMap.isEmpty()) {
                return returnValue;
            }
            for (Object o : returnValueList) {
                for (Map.Entry<String, String> entry : internationalFieldMap.entrySet()) {
                    Object valueByGetMethod = ReflectUtils.getValueByGetMethod(entry.getKey(), o);
                    if (valueByGetMethod instanceof String) {
                        String value = valueByGetMethod.toString();
                        String[] splitValue = value.split(entry.getValue());
                        int languageIndex = AppContext.getClientLanguage().getIndex();
                        if (languageIndex < splitValue.length) {
                            ReflectUtils.setValueBySetMethod(entry.getKey(), o, splitValue[languageIndex]);
                        } else {
                            ReflectUtils.setValueBySetMethod(entry.getKey(), o, "");
                        }

                    }
                }
                for (Map.Entry<String, SensitizedType> entry : desensitizedFieldMap.entrySet()) {
                    Object valueByGetMethod = ReflectUtils.getValueByGetMethod(entry.getKey(), o);
                    if (valueByGetMethod instanceof String) {
                        String value = valueByGetMethod.toString();
                        switch (entry.getValue()) {
                            case mobilePhone:
                                ReflectUtils.setValueBySetMethod(entry.getKey(), o, DesensitizedUtil.mobilePhone(value));
                                break;
                            case email:
                                ReflectUtils.setValueBySetMethod(entry.getKey(), o, DesensitizedUtil.email(value));
                                break;
                            case chineseName:
                                ReflectUtils.setValueBySetMethod(entry.getKey(), o, DesensitizedUtil.chineseName(value));
                                break;
                            case idCardNum:
                                ReflectUtils.setValueBySetMethod(entry.getKey(), o, DesensitizedUtil.idCardNum(value, 6, 4));
                                break;
                        }
                    }
                }
            }
        }
        return returnValue;
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
