package cn.acyou.leo.framework.config;

import cn.acyou.leo.framework.cache.MyRedisCacheManager;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.*;

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
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        //FastJsonRedisSerializer<Object> valueSerializer = new FastJsonRedisSerializer<>(Object.class);
        //Jackson序列化
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        valueSerializer.setObjectMapper(objectMapper);
        //JDK序列化
        //JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }

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
