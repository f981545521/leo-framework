package cn.acyou.leo.framework.util;

import cn.hutool.core.map.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <pre>
 *         MapListBuild&lt;String, String&gt; mapList1 = MapUtils.createMapList();
 *         mapList1.putValue("1", "1");
 *         mapList1.putValue("1", "2");
 *         mapList1.putValue("1", "3");
 *         mapList1.putValue("1", "4");
 *         mapList1.putValue("2", "1");
 *         mapList1.putValue("2", "2");
 *         mapList1.putValue("2", "3");
 *         mapList1.putValue("2", "4");
 *         mapList1.putValue("3", "1")
 *                 .putValue("3", "2")
 *                 .putValue("3", "2")
 *                 .putValue("3", "2");
 *
 *         System.out.println(mapList1.getMap());
 *
 *         Map&lt;String, String&gt; map = MapUtils.createMap("1", "2");
 *         System.out.println(map);
 *         Map&lt;String, Integer&gt; map1 = MapUtils.&lt;String, Integer&gt;createMap().putValue("1", 2).putValue("2", 3).getMap();
 *         System.out.println(map1);
 * </pre>
 * @author fangyou
 * @version [1.0.0, 2021-08-24 13:53]
 */
public class MapUtils extends MapUtil{
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


    public static Map<String, String> createMap(String k, String v){
        Map<String, String> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    /** ********************************** 构建 Map<K, V>****************************************************** */
    public static <K, V> MapBuild<K, V> createMap() {
        return new MapBuild<>(new HashMap<>());
    }

    public static <K, V> MapBuild<K, V> createMap(Map<K, V> map) {
        return new MapBuild<>(map);
    }

    public static class MapBuild<K, V> {
        private final Map<K, V> map;

        public MapBuild(Map<K, V> map) {
            this.map = map;
        }

        public MapBuild<K, V> putValue(K k, V v){
            map.put(k, v);
            return this;
        }

        public Map<K, V> getMap(){
            return map;
        }
    }


    /** ********************************** 构建 Map<K, List<V>>****************************************************** */
    public static <K, V> MapListBuild<K, V> createMapList() {
        return new MapListBuild<>(new HashMap<>());
    }

    public static <K, V> MapListBuild<K, V> createMapList(Map<K, List<V>> map) {
        return new MapListBuild<>(map);
    }

    public static class MapListBuild<K, V> {
        private final Map<K, List<V>> map;

        public MapListBuild(Map<K, List<V>> map) {
            this.map = map;
        }

        public MapListBuild<K, V> putValue(K k, V v){
            if (map.containsKey(k)) {
                map.get(k).add(v);
            }else {
                List<V> vList = new ArrayList<>();
                vList.add(v);
                map.put(k, vList);
            }
            return this;
        }

        public Map<K, List<V>> getMap(){
            return map;
        }
    }

    /**
     * 为Map<K, List<V>> 添加值
     */
    public static <K, V> Map<K, List<V>> putList(Map<K, List<V>> map, K k, V v){
        if (map == null) {
            map = new HashMap<>();
        }
        if (map.containsKey(k)) {
            map.get(k).add(v);
        }else {
            List<V> vList = new ArrayList<>();
            vList.add(v);
            map.put(k, vList);
        }
        return map;
    }

    /**
     * 为Map<K, List<V>> 初始化
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> initList(Map<K, List<V>> map, K... keys){
        if (keys != null && keys.length > 0) {
            for (K key : keys) {
                map.put(key, new ArrayList<>());
            }
        }
        return map;
    }


}
