package cn.acyou.leo.framework.wx.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 14:07]
 */
public class WxCommonCachePool {
    /**
     * 微信内部缓存池
     */
    private static final ConcurrentHashMap<String, WxCacheItem> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取缓存值
     *
     * @param key 缓存Key
     * @return 缓存value
     */
    public static String get(String key) {
        WxCacheItem wxCacheItem = CACHE_MAP.get(key);
        if (wxCacheItem != null) {
            if (wxCacheItem.getExpiresTime() == null) {
                return wxCacheItem.getCacheValue();
            }
            if (wxCacheItem.getExpiresTime() > System.currentTimeMillis()) {
                return wxCacheItem.getCacheValue();
            } else {
                CACHE_MAP.remove(key);
            }
        }
        return null;
    }

    /**
     * 设置缓存值
     *
     * @param key   缓存Key
     * @param value 缓存value
     */
    public static void put(String key, String value) {
        WxCacheItem wxCacheItem = new WxCacheItem(value, null);
        CACHE_MAP.put(key, wxCacheItem);
    }


    /**
     * 设置缓存值（带过期时间）
     *
     * @param key      缓存Key
     * @param value    缓存value
     * @param duration duration
     * @param timeUnit 单位
     */
    public static void put(String key, String value, long duration, TimeUnit timeUnit) {
        long toMillis = timeUnit.toMillis(duration);
        WxCacheItem wxCacheItem = new WxCacheItem(value, System.currentTimeMillis() + toMillis);
        CACHE_MAP.put(key, wxCacheItem);
    }


    /**
     * 获取和缓存
     *
     * @param key      关键
     * @param function 函数
     * @return {@link String}
     */
    public static String getAndCache(String key, Function<String, String> function) {
        String v = get(key);
        if (v != null && v.length() > 0) {
            return v;
        }
        String value = function.apply(key);
        put(key, value, 7200, TimeUnit.SECONDS);
        return value;
    }

    /**
     * 清除缓存
     */
    public static void clearAllCache() {
        CACHE_MAP.clear();
    }

    /**
     * 清除缓存
     */
    public static void clearCache(String key) {
        CACHE_MAP.remove(key);
    }

    /**
     * 清除缓存
     */
    public static void clearCache(List<String> keys) {
        keys.forEach(CACHE_MAP::remove);
    }

}
