package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.cache.MyRedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置类
 * <p>
 * RedisAutoConfiguration中已经包含了：
 * <ul>
 *     <li>redisTemplate {@link RedisAutoConfiguration#redisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory)}</li>
 *     <li>stringRedisTemplate {@link RedisAutoConfiguration#stringRedisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory)}</li>
 * </ul>
 *
 * @author youfang
 * @date 2018/12/10 16:54
 **/
//@Configuration
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnBean(RedisConnectionFactory.class) //@ConditionalOnBean不可靠和Bean的加载顺序有关系
@AutoConfigureAfter({RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    /**
     * EnableCaching 缓存管理器（RedisCacheManager）
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //缓存默认有效期 24小时
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24));
        //配置序列化
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        //构建缓存管理器
        //return RedisCacheManager.builder(redisCacheWriter).cacheDefaults(redisCacheConfiguration).transactionAware().build();
        //自定义缓存管理器， 支持自定义过期时间：@Cacheable(value="sys:student#100", key="#id") 100S过期
        //                                     @Cacheable(value="sys:student#-1", key="#id")  永不过期
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        return new MyRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }


}
