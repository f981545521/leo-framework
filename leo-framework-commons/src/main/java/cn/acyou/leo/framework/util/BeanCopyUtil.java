package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Bean 之间的拷贝
 *
 * @author acyou
 */
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
            E e = clz.newInstance();
            BeanUtils.copyProperties(t, e);
            return e;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //return null;会导致编译器空指针检查警告，理论上不会出现此情况，所以抛出异常。
        throw new ServiceException("BeanCopy 出错了！");
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
     * 合并属性
     * @param <M>   对象类型
     * @param target      目标
     * @param destination 目的
     */
    public static <M> void merge(M target, M destination) {
        merge(target, destination, false);
    }

    /**
     * 合并属性
     *
     * @param <M>   对象类型
     * @param target          目标
     * @param destination     目的地
     * @param nullCoverTarget 使用null覆盖目标
     */
    public static <M> void merge(M target, M destination, boolean nullCoverTarget) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (descriptor.getWriteMethod() != null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(destination);
                    if (defaultValue == null && !nullCoverTarget) {
                        continue;
                    }
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
