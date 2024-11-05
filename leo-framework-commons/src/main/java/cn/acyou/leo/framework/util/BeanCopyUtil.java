package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Bean 之间的拷贝
 *
 * @author youfang
 */
@Slf4j
public class BeanCopyUtil {

    /**
     * 复制
     *
     * @param <E>   目标对象
     * @param <T>   源对象
     * @param t   源对象
     * @param clz 目标对象Class
     * @return 目标对象
     */
    public static <T, E> E copy(T t, Class<E> clz) {
        if (t == null) {
            return null;
        }
        try {
            E instance = clz.newInstance();
            BeanUtils.copyProperties(t, instance);
            return instance;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //return null;会导致编译器空指针检查警告，理论上不会出现此情况，所以抛出异常。
        throw new ServiceException("BeanCopy出错了！{}->{}", t, clz.getName());
    }

    /**
     * 复制对象List
     *
     * @param <E>   目标对象
     * @param <T>   源对象
     * @param l   源对象集合
     * @param clz 目标对象Class
     * @return 目标对象集合
     */
    public static <T, E> List<E> copyList(Collection<T> l, Class<E> clz) {
        List<E> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(l)) {
            l.forEach(item -> list.add(copy(item, clz)));
        }
        return list;
    }

    /**
     * 以JSON的方式复制对象
     *
     * @param <E> 目标对象
     * @param o   源对象
     * @param clz 目标对象Class
     * @return 目标对象集合
     */
    public static <E> E copyByJson(Object o, Class<E> clz) {
        String json = JSON.toJSONString(o);
        return JSON.parseObject(json, clz);
    }

    /**
     * 以JSON的方式复制对象（对于泛型对象）
     *
     * @param <E>           目标对象
     * @param o             源对象
     * @param typeReference 目标对象Class
     * @return 目标对象集合
     */
    public static <E> E copyByJson(Object o, TypeReference<E> typeReference) {
        String json = JSON.toJSONString(o);
        return JSON.parseObject(json, typeReference);
    }


    /**
     * 合并同一个对象的属性 (不会使用null覆盖目标)
     *
     * @param <M> 对象类型
     * @param source 被合并对象
     * @param target 合并到
     */
    public static <M> void merge(M source, M target) {
        merge(source, target, false);
    }

    /**
     * 合并同一个对象的属性
     *
     * @param <M> 对象类型
     * @param source 被合并对象
     * @param target 合并到
     * @param nullCoverTarget 使用null覆盖目标
     */
    public static <M> void merge(M source, M target, boolean nullCoverTarget) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (descriptor.getWriteMethod() != null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(target);
                    if (defaultValue == null && !nullCoverTarget) {
                        continue;
                    }
                    descriptor.getWriteMethod().invoke(source, defaultValue);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 复制Bean属性
     * @param source 源
     * @param target 目标
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 复制Bean属性（null值不复制）
     * @param source 源
     * @param target 目标
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取对象所有null属性的名称
     * @param source 对象
     * @return 所有null属性的名称
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
