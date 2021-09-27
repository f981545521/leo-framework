package cn.acyou.leo.framework.cache;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/1]
 **/
public class CustomizeRedisCache extends RedisCache {
    private final RedisCacheWriter redisCacheWriter;
    private final RedisCacheConfiguration configuration;
    //校验规则：获取时间
    private static final String SPLITTER = "#";

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected CustomizeRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        redisCacheWriter = cacheWriter;
        configuration = cacheConfig;
    }


    /**
     * 重写cache put 逻辑，引入自定义TTL 实现
     * 实现逻辑:
     * 1.通过获取@Cacheable 中的value ,然后根据约定好的特殊字符进行分割
     * 2.从分割结果集中获取设置的TTL 时间并将value 中的，然后给当前缓存设置TTL
     */
    @Override
    public void put(Object key, Object value) {
        String name = super.getName();
        //是否按照指定的格式
        if (name.contains(SPLITTER)) {
            List<String> keyList = Arrays.asList(name.split(SPLITTER));
            if (keyList.size() != 2) {
                throw new IllegalArgumentException("@Cacheable value = " + name + " not Allow !");
            }
            //获取键值
            String finalName = keyList.get(0);
            //获取TTL 执行时间
            String expireName = keyList.get(1);
            if (!NumberUtils.isCreatable(expireName)) {
                //不是时间类型
                throw new IllegalArgumentException("@Cacheable value = " + name + " not Allow ! ttl invalid");
            }
            long ttl = Long.parseLong(expireName);

            //获取缓存value
            Object cacheValue = preProcessCacheValue(value);
            //获取value 为null 时，抛出异常
            if (!isAllowNullValues() && cacheValue == null) {
                throw new IllegalArgumentException(String.format(
                        "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                        name));
            }
            //插入时添加时间
            redisCacheWriter.put(finalName, serializeCacheKey(createCacheKey(key)), serializeCacheValue(cacheValue), Duration.ofSeconds(ttl));

        }else {
            //原来逻辑处理
            super.put(key, value);
        }
    }

    @Override
    protected String createCacheKey(Object key) {
        String convertedKey = convertKey(key);
        if (!configuration.usePrefix()) {
            return convertedKey;
        }
        return prefixCacheKey(convertedKey);
    }


    private String prefixCacheKey(String key) {
        String name = super.getName();
        if (name.contains(SPLITTER)) {
            List<String> keyList = Arrays.asList(name.split(SPLITTER));
            String finalName = keyList.get(0);
            return configuration.getKeyPrefixFor(finalName) + key;
        }
        // allow contextual cache names by computing the key prefix on every call.
        return configuration.getKeyPrefixFor(name) + key;
    }
}
