package cn.acyou.leo.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static MapListBuild createMapList() {
        return new MapListBuild<>(new HashMap<>());
    }

    public static <K, V> MapListBuild createMapList(Map<K, List<V>> map) {
        return new MapListBuild<>(map);
    }

    public static class MapListBuild<K, V> {
        private final Map<K, List<V>> map;

        public MapListBuild(Map<K, List<V>> map) {
            this.map = map;
        }

        public MapListBuild putValue(K k, V v){
            if (map.containsKey(k)) {
                map.get(k).add(v);
            }else {
                List<V> vList = new ArrayList<>();
                vList.add(v);
                map.put(k, vList);
            }
            return this;
        }
    }

}
