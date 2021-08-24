package cn.acyou.leo.framework.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 为集合实例提供实用工具方法。
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-24 13:43]
 */
public class CollectionUtils {
    /**
     * 不可修改的空集合。JDK提供了可用于此目的的空集和列表实现。但是，它们可能被强制转换为集合或列表，这可能是不需要的。此实现仅实现集合。
     * @param <T> 类型
     * @return 空集合
     */
    public static <T> Collection<T> emptyCollection() {
        return Collections.emptyList();
    }

    /**
     * 空安全检查指定的集合是否为空。 Null返回true。
     *
     * @param coll  要检查的集合可能为空
     * @return 如果为空或null，则为true
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 不是空的
     *
     * @param coll 要检查的集合可能为空
     * @return 如果为非null和非空，则为true
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 删除所有
     *
     * @param collection 集合
     * @param remove     删除的集合
     * @return 处理后的集合
     */
    public static <E> List<E> removeAll(final Collection<E> collection, final Collection<?> remove) {
        final List<E> list = new ArrayList<>();
        for (final E obj : collection) {
            if (!remove.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

}
