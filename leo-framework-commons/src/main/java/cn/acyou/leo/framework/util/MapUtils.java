package cn.acyou.leo.framework.util;

import java.util.Map;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-24 13:53]
 */
public class MapUtils {
    /**
     * 是空的
     *
     * @param map 要检查的Map,可能为空
     * @return 如果为空或null，则为true
     */
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 不是空的
     *
     * @param map 要检查的Map,可能为空
     * @return 如果为非null和非空，则为true
     */
    public static boolean isNotEmpty(final Map<?,?> map) {
        return !isEmpty(map);
    }
}
