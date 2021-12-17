package cn.acyou.leo.framework.advisor;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 9:31]
 **/
@Component
public class RedisLockSource {

    private final Map<Object, RedisLockMetadata> metadataCache = new ConcurrentHashMap<>(1024);

    public RedisLockMetadata getRedisLockMetadata(Method method, @Nullable Class<?> targetClass) {
        Object metadataKey = getMetadataKey(method, targetClass);
        RedisLockMetadata metadata = metadataCache.get(metadataKey);
        if (metadata == null) {
            Set<RedisLock> anns = AnnotatedElementUtils.getAllMergedAnnotations(method, RedisLock.class);
            if (!CollectionUtils.isEmpty(anns)) {
                for (RedisLock ann : anns) {
                    metadata = new RedisLockMetadata(ann.expire(), ann.key(), ann.waitTime());
                    metadataCache.put(metadataKey, metadata);
                }
            }
        }
        return metadata;
    }

    private static Object getMetadataKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    @Data
    @AllArgsConstructor
    public static class RedisLockMetadata {
        private int expire;
        private String key;
        private int waitTime;
    }
}
