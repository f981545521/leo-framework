package cn.acyou.leo.framework.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * <p>自定义缓存管理， 支持自定义过期时间。</p>
 * <p>example：</p>
 *
 *  <pre>
 *      @Cacheable(value="sys:student#100", key="#id") 100S过期
 *      @Cacheable(value="sys:student#-1", key="#id")  永不过期
 *  </pre>
 * @author youfang
 * @version [1.0.0, 2020/7/1]
 **/
public class MyRedisCacheManager extends RedisCacheManager {
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    /**
     * 用于返回自定义的redisCache
     */
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new CustomizeRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }
}
