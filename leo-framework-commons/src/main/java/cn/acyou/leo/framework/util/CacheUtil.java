package cn.acyou.leo.framework.util;

import lombok.Data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 本机缓存
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-04 14:07]
 */
public class CacheUtil {
    /**
     * 缓存池
     */
    private static final ConcurrentHashMap<String, CacheBean> CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 默认缓存 7200 s
     */
    private static final long DEFAULT_CACHE_DURATION = 7200;

    /**
     * 获取缓存值
     *
     * @param key 缓存Key
     * @return 缓存value
     */
    public synchronized static String get(String key) {
        CacheBean cacheBean = CACHE_MAP.get(key);
        if (cacheBean != null) {
            if (cacheBean.getExpiresTime() == null) {
                return cacheBean.getCacheValue();
            }
            if (cacheBean.getExpiresTime() > System.currentTimeMillis()) {
                return cacheBean.getCacheValue();
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
        CacheBean cacheBean = new CacheBean(value, null);
        CACHE_MAP.put(key, cacheBean);
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
        CacheBean cacheBean = new CacheBean(value, System.currentTimeMillis() + toMillis);
        CACHE_MAP.put(key, cacheBean);
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
        put(key, value, DEFAULT_CACHE_DURATION, TimeUnit.SECONDS);
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

    @Data
    public static class CacheBean {

        /**
         * 缓存值
         */
        private String cacheValue;
        /**
         * 过期时间(null 表示长期)
         */
        private Long expiresTime;

        public CacheBean(String cacheValue) {
            this.cacheValue = cacheValue;
            this.expiresTime = null;
        }

        public CacheBean(String cacheValue, Long expiresTime) {
            this.cacheValue = cacheValue;
            this.expiresTime = expiresTime;
        }
    }
}
