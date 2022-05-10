package cn.acyou.leo.framework.util.redis;

import cn.acyou.leo.framework.exception.ConcurrentException;
import cn.acyou.leo.framework.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis 命令参考
 * http://doc.redisfans.com/
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-25 13:50]
 */
@Component
public class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);
    /**
     * 默认锁 前缀标识
     */
    private static final String LOCK_KEY_PREFIX = "REDIS_LOCK:";
    /**
     * 默认锁 超时时间 60S
     */
    public static final int DEFAULT_LOCK_TIME_OUT = 60 * 1000;
    /**
     * 默认 getAndCache 超时时间 {@link #getAndCache(String, Function)}
     */
    private static final int DEFAULT_CACHE_SECONDS = 60 ;
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisObjTemplate;

    //私有化构造方法，无法通过new创建。而不影响Spring通过反射创建Bean
    private RedisUtils() {
        log.info("RedisUtils 初始化完成。");
    }

    /**
     * ZSet 类型操作
     */
    public ZSetOperations<String, String> opsForZset() {
        return redisTemplate.opsForZSet();
    }

    /**
     * String 类型操作
     */
    public ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }

    /**
     * Hash 类型操作
     */
    public HashOperations<String, Object, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * List 类型操作
     */
    public ListOperations<String, String> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * Set 类型操作
     */
    public SetOperations<String, String> opsForSet() {
        return redisTemplate.opsForSet();
    }

    /**
     * Geo 类型操作 (经纬度)
     */
    public GeoOperations<String, String> opsForGeo() {
        return redisTemplate.opsForGeo();
    }


    /**
     * 从当前数据库中随机返回(不删除)一个 key 。
     *
     * @return 当数据库不为空时，返回一个 key 。
     * 当数据库为空时，返回 nil 。
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 统计存在的键
     *
     * @param keys 键
     * @return 存在的键的数量
     */
    public Long countExistingKeys(Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    /**
     * 删除
     *
     * @param keys 键
     * @return 删除的数量
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 删除
     *
     * @param key 键
     * @return 成功/失败
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 包含键
     *
     * @param key 键
     * @return 是/否
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 为键设置过期时间
     *
     * @param key     关键
     * @param timeout 超时
     * @return 成功/失败
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    /**
     * 为键设置过期时间
     *
     * @param key     关键
     * @param timeout 时间
     * @param unit    单位
     * @return 成功/失败
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 为键设置过期时间
     *
     * @param key  关键
     * @param date 日期
     * @return 成功/失败
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @param key 关键
     * @return 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @param key  关键
     * @param unit 单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 查找所有符合给定模式 pattern 的 key 。
     * <pre>
     * KEYS * 匹配数据库中所有 key 。
     * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
     * KEYS h*llo 匹配 hllo 和 heeeeello 等。
     * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
     * 特殊符号用 \ 隔开
     * </pre>
     *
     * @param pattern 模式
     * @return 符合给定模式的 key 列表。
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
     *
     * @param key 关键
     * @return <p>当生存时间移除成功时，返回 1 .</p>
     * <p>如果 key 不存在或 key 没有设置生存时间，返回 0 。</p>
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }


    /**
     * 将 key 改名为 newkey 。
     * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。
     * 当 newkey 已经存在时， RENAME 命令将覆盖旧值。
     * <p>
     * 改名成功时提示 OK ，失败时候返回一个错误。
     *
     * @param oldKey 旧的关键
     * @param newKey 新的密钥
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 当且仅当 newkey 不存在时，将 key 改名为 newkey 。
     *
     * @param oldKey 旧的关键
     * @param newKey 新的密钥
     * @return 修改成功时，返回 1 。
     * 如果 newkey 已经存在，返回 0 。
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }


    /**
     * 排序
     *
     * @param key      关键
     * @param order    排序
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @return 元素
     */
    public List<String> sort(String key, SortParameters.Order order, long pageNum, long pageSize) {
        long offset = (pageNum - 1) * pageSize;
        SortQuery<String> sortQuery = SortQueryBuilder.sort(key).order(order).limit(offset, pageSize).build();
        return redisTemplate.sort(sortQuery);
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key 关键
     * @return 类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * <p>
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key   键
     * @param value 值
     * @return 追加 value 之后， key 中字符串的长度。
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 将 key 中储存的数字值减一。
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     *
     * @param key 关键
     * @return 执行 DECR 命令之后 key 的值。
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 将 key 所储存的值减去减量 decrement 。
     *
     * @param key  关键
     * @param deal 减量
     * @return 减去 decrement 之后， key 的值。
     */
    public Long decrement(String key, long deal) {
        return redisTemplate.opsForValue().decrement(key, deal);
    }

    /**
     * 将 key 中储存的数字值增一。
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     *
     * @param key 关键
     * @return 执行 INCR 命令之后 key 的值。
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 将 key 所储存的值加上增量 increment 。
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     *
     * @param key  键
     * @param deal 增量
     * @return 加上 increment 之后， key 的值。
     */
    public Long increment(String key, long deal) {
        return redisTemplate.opsForValue().increment(key, deal);
    }
    /**
     * 自增 / 自减 并在初始时设置过期时间
     * 保证原子性：初始化值为1的时候必设置过期时间。
     * @param key   key
     * @param delta 1自增1 -1减少1
     * @param timeOut 超时时间（单位秒）
     * @return 执行 INCR 命令之后 key 的值。
     */
    public Long increment(String key, long delta, long timeOut) {
        SessionCallback<Long> sessionCallback = new SessionCallback<Long>() {
            @Override
            public Long execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTemplate.opsForValue().increment(key, delta);
                List<?> exec = operations.exec();
                Long incValue = (Long) exec.get(0);
                if (incValue != null && incValue == 1){
                    redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
                }
                return incValue;
            }
        };
        return redisTemplate.execute(sessionCallback);
    }
    /**
     * 返回 key 所关联的字符串值。
     *
     * @param key 关键
     * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。
     * 如果 key 不是字符串类型，那么返回一个错误。
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     *
     * @param key   键
     * @param value 值
     * @return 返回给定 key 的旧值。
     */
    public String getAndSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 返回所有(一个或多个)给定 key 的值。
     *
     * @param keys 键
     * @return 一个包含所有给定 key 的值的列表。
     */
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 同时设置一个或多个 key-value 对。
     *
     * @param keyValues 键值
     */
    public void multiSet(Map<String, String> keyValues) {
        redisTemplate.opsForValue().multiSet(keyValues);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     *
     * @param keyValues 键值
     * @return 成功/失败
     */
    public Boolean multiSetIfAbsent(Map<String, String> keyValues) {
        return redisTemplate.opsForValue().multiSetIfAbsent(keyValues);
    }

    /**
     * 将字符串值 value 关联到 key 。
     * <p>
     * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     * <p>
     * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     *
     * @param key   关键
     * @param value 价值
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值 value 关联到 key ，并设置 key 的生存时间。
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时
     * @param unit    单位
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     *
     * @param key   关键
     * @param value 价值
     */
    public Boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。并设置 key 的生存时间。
     *
     * @param key     关键
     * @param value   价值
     * @param timeout 超时
     * @param unit    单位
     */
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 如果存在设置
     *
     * @param key   关键
     * @param value 价值
     */
    public Boolean setIfPresent(String key, String value) {
        return redisTemplate.opsForValue().setIfPresent(key, value);
    }

    /**
     * 如果存在设置，并设置 key 的生存时间。
     *
     * @param key     关键
     * @param value   价值
     * @param timeout 超时
     * @param unit    单位
     */
    public Boolean setIfPresent(String key, String value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfPresent(key, value, timeout, unit);
    }

    /**
     * 获取值的长度：<code>STRLEN</code>
     *
     * @param key 关键
     * @return 值的长度
     */
    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /*
     * ——————————————————————————Hash——————————————————————————————
     */


    /**
     * 散列的大小
     *
     * @param key 关键
     * @return 所有hash键的数量
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 散列删除
     *
     * @param key      关键
     * @param hashKeys 散列键
     * @return 已删除的数量
     */
    public Long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 散列有关键
     *
     * @param key      关键
     * @param hashKeys 散列键
     * @return 是/否
     */
    public Boolean hashHasKey(String key, Object hashKeys) {
        return redisTemplate.opsForHash().hasKey(key, hashKeys);
    }

    /**
     * 哈希得到
     *
     * @param key      关键
     * @param hashKeys 散列键
     * @return 哈希值
     */
    public Object hashGet(String key, Object hashKeys) {
        return redisTemplate.opsForHash().get(key, hashKeys);
    }

    /**
     * 散列多得到
     *
     * @param key      关键
     * @param hashKeys 散列键
     * @return 哈希值列表
     */
    public List<Object> hashMultiGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    /**
     * 散列增量
     *
     * @param key     关键
     * @param hashKey 散列键
     * @param delta   δ
     * @return 增量后的值
     */
    public Long hashIncrement(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 散列键
     *
     * @param key 关键
     * @return 值集合
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 哈希设置键值
     *
     * @param key     关键
     * @param hashKey 散列键
     * @param value   价值
     */
    public void hashPut(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 哈希设置键值 如果缺席
     *
     * @param key     关键
     * @param hashKey 散列键
     * @param value   价值
     */
    public void hashPutIfAbsent(String key, String hashKey, String value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 哈希批量设置键值
     *
     * @param key     关键
     * @param entries 条目
     */
    public void hashPutAll(String key, Map<String, String> entries) {
        redisTemplate.opsForHash().putAll(key, entries);
    }

    /**
     * hash的值列表
     *
     * @param key 关键
     * @return 值列表
     */
    public List<Object> hashValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 哈希条目
     *
     * @param key 关键
     * @return 所有条目
     */
    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /*
     * ——————————————————————————List——————————————————————————————
     */

    /**
     * 列表设置
     *
     * @param key   关键
     * @param index 索引
     * @param value 价值
     */
    public void listSet(String key, long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 列表索引下的元素
     *
     * @param key   关键
     * @param index 索引
     * @return 元素
     */
    public String listIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 移除并返回列表 key 的头元素。
     *
     * @param key 关键
     * @return 元素
     */
    public String listLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除并返回列表 key 的头元素。
     *
     * @param key     关键
     * @param timeout 超时
     * @param unit    单位
     * @return 元素
     */
    public String listLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除并返回列表 key 的尾元素。
     *
     * @param key 关键
     * @return 元素
     */
    public String listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除并返回列表 key 的尾元素。
     *
     * @param key     关键
     * @param timeout 超时
     * @param unit    单位
     * @return 元素
     */
    public String listRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 列表值的索引
     *
     * @param key   关键
     * @param value 价值
     * @return 索引
     */
    public Long listIndexOf(String key, String value) {
        return redisTemplate.opsForList().indexOf(key, value);
    }

    /**
     * 列表值的索引，倒序
     *
     * @param key   关键
     * @param value 价值
     * @return {索引
     */
    public Long listLastIndexOf(String key, String value) {
        return redisTemplate.opsForList().lastIndexOf(key, value);
    }

    /**
     * 将一个值 value 插入到列表 key 的表头
     *
     * @param key   关键
     * @param value 价值
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将一个值 value 插入到指定元素的前面
     *
     * @param key   关键
     * @param pivot 主
     * @param value 价值
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 将多个值 value 插入到列表 key 的表头
     *
     * @param key    关键
     * @param values 值
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPush(String key, String... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 将多个值 value 插入到列表 key 的表头
     *
     * @param key    关键
     * @param values 值
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPush(String key, Collection<String> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 将一个值 value 插入到指定元素的前面。如果存在
     *
     * @param key   关键
     * @param value 价值
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long leftPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 将一个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key   关键
     * @param value 价值
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long listRightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将一个值 value 插入到列表 指定元素的后面
     *
     * @param key   关键
     * @param pivot 主
     * @param value 价值
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long listRightPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 将多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key    关键
     * @param values 值
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long listRightPushAll(String key, String... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 将多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key    关键
     * @param values 值
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long listRightPushAll(String key, Collection<String> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 将一个值 value 插入到列表 指定元素的后面，如果存在列表
     *
     * @param key   关键
     * @param value 价值
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long listRightPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     *
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return 元素
     */
    public List<String> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 列表的大小
     *
     * @param key 关键
     * @return 长度
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 列表中删除
     *
     * @param key   关键
     * @param count 数
     * @param value 价值
     * @return 移除元素的个数
     */
    public Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /*
     * ——————————————————————————Set——————————————————————————————
     */

    /**
     * set 添加
     *
     * @param key    关键
     * @param values 值
     * @return set的长度
     */
    public Long setAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * SCARD
     * <p>
     * 返回集合 key 的基数(集合中元素的数量)。
     *
     * @param key 关键
     * @return 集合的基数。
     * 当 key 不存在时，返回 0 。
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     *
     * @param key    关键
     * @param values 值
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * set的成员
     *
     * @param key 关键
     * @return 元素
     */
    public Set<String> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * SDIFF
     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
     * 不存在的 key 被视为空集。
     *
     * @param key      关键
     * @param otherKey 其他关键
     * @return 差集的成员列表
     */
    public Set<String> setDifference(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * SINTER
     * 返回一个集合的全部成员，该集合是所有给定集合的交集。
     * 不存在的 key 被视为空集。
     *
     * @param key      关键
     * @param otherKey 其他关键
     * @return 交集成员的列表。
     */
    public Set<String> setIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * SUNION
     * 返回一个集合的全部成员，该集合是所有给定集合的并集。
     *
     * @param key      关键
     * @param otherKey 其他关键
     * @return 并集成员的列表。
     */
    public Set<String> setUnion(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * SISMEMBER
     * 判断 member 元素是否集合 key 的成员。
     *
     * @param key   关键
     * @param value 价值
     * @return 是/否
     */
    public Boolean setIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * set 移动
     *
     * @param key     关键
     * @param value   价值
     * @param destKey 关键不在座位上
     * @return 是否成功
     */
    public Boolean setMove(String key, String value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 移除并返回集合中的一个随机元素。
     *
     * @param key 关键
     * @return 被移除的随机元素。
     */
    public String setPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 移除并返回集合中的多个随机元素。
     *
     * @param key   关键
     * @param count 数
     * @return 被移除的随机元素列表
     */
    public List<String> setPop(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    /*
     * ——————————————————————————SortedSet（有序集合）——————————————————————————————
     */

    /**
     * zSet 大小
     *
     * @param key 关键
     * @return 长度
     */
    public Long zSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * zSet 添加
     *
     * @param key   关键
     * @param value 价值
     * @param score 分数
     * @return 成功/失败
     */
    public Boolean zSetAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     *
     * @param key       key
     * @param zSetItems 输入元组
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    public Long zSetAdd(String key, Collection<ZSetItem> zSetItems) {
        return redisTemplate.opsForZSet().add(key, buildTypedTuple(zSetItems));
    }

    /**
     * zSet 删除元素
     *
     * @param key    关键
     * @param values 值
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zSetRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * zSet 删除范围元素
     *
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zSetRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * zSet 根据分数范围删除元素
     *
     * @param key 关键
     * @param min 最小值
     * @param max 最大值
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zSetRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * zSet 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
     *
     * @param key 关键
     * @param min 最小值
     * @param max 最大值
     * @return score 值在 min 和 max 之间的成员的数量。
     */
    public Long zSetCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * ZINCRBY
     * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
     * <p>
     * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
     * 当 key 不是有序集类型时，返回一个错误。
     * score 值可以是整数值或双精度浮点数。
     *
     * @param key    键
     * @param member value
     * @param deal   value
     * @return member 成员的新 score 值。
     */
    public Double zSetIncrementScore(String key, String member, double deal) {
        return redisTemplate.opsForZSet().incrementScore(key, member, deal);
    }

    /**
     * 返回一个成员范围的有序集合（由字典范围）
     *
     * @param key   key
     * @param range range
     * @param limit limit
     * @return 范围的Value集合
     */
    public Set<String> zSetRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return redisTemplate.opsForZSet().rangeByLex(key, range, limit);
    }

    /**
     * 按分数返回一个成员范围的有序集合。
     * <p>
     * 返回Value集合，以分数排序从低分到高分排序
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 开始
     * @param count  数量
     * @return 范围的Value集合。
     */
    public Set<String> zSetRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * zSet 按照分数升序排序
     *
     * @param key      关键
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return key列表
     */
    public Set<String> zSetOrderByScoreAsc(String key, int pageNum, int pageSize) {
        long offset = (long) (pageNum - 1) * pageSize;
        return zSetRangeByScore(key, 0, Double.MAX_VALUE, offset, pageSize);
    }

    /**
     * zSet 按照分数降序排序
     *
     * @param key      关键
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return key列表
     */
    public Set<String> zSetOrderByScoreDesc(String key, int pageNum, int pageSize) {
        long offset = (long) (pageNum - 1) * pageSize;
        return zSetReverseRangeByScore(key, 0, Double.MAX_VALUE, offset, pageSize);
    }

    /**
     * ZRANK
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * <p>
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     * <p>
     * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
     *
     * @param key 关键
     * @param v   v
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名。
     * 如果 member 不是有序集 key 的成员，返回 nil 。
     */
    public Long zSetRank(String key, Object v) {
        return redisTemplate.opsForZSet().rank(key, v);
    }

    /**
     * zSet 反向排名
     *
     * @param key 关键
     * @param v   v
     * @return 排名
     */
    public Long zSetReverseRank(String key, Object v) {
        return redisTemplate.opsForZSet().reverseRank(key, v);
    }

    /**
     * 根据分数从大到小 获取前几个
     *
     * @param key   key
     * @param limit 前几
     * @return value集合
     */
    public Set<String> zSetReverseRangeLimit(String key, long limit) {
        return redisTemplate.opsForZSet().reverseRange(key, 0, limit);
    }

    /**
     * 返回一个成员范围的有序集合，通过索引，以分数排序，从高分到低分
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return value 集合
     */
    public Set<String> zSetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 返回Value集合，以分数排序从高分到低分排序
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 开始
     * @param count  数量
     * @return 范围的Value集合。
     */
    public Set<String> zSetReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * 其中成员的位置按 score 值递增(从大到小)来排序。
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 成员和它的 score 值 集合
     */
    public List<ZSetItem> zSetReverseRangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<String>> sset = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (sset == null) {
            return new ArrayList<>();
        }
        return buildZsetList(sset);
    }


    /**
     * 封装Zset 查询结果
     *
     * @param zset zset
     * @return List<ZSetItem>
     */
    private List<ZSetItem> buildZsetList(Set<ZSetOperations.TypedTuple<String>> zset) {
        List<ZSetItem> result = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> stringTypedTuple : zset) {
            ZSetItem item = new ZSetItem();
            String value = stringTypedTuple.getValue();
            Double score = stringTypedTuple.getScore();
            item.setValue(value);
            item.setScore(score);
            result.add(item);
        }
        return result;
    }

    /**
     * 封装ZSetItem为 TypedTuple类型
     *
     * @param zSetItems zset
     * @return TypedTuple
     */
    private Set<ZSetOperations.TypedTuple<String>> buildTypedTuple(Collection<ZSetItem> zSetItems) {
        Set<ZSetOperations.TypedTuple<String>> result = new HashSet<>();
        for (ZSetItem zSetItem : zSetItems) {
            ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>(zSetItem.getValue(), zSetItem.getScore());
            result.add(typedTuple);
        }
        return result;
    }

    /* *********************************** Redis 缓存  **************************** */

    /**
     * 获取和缓存
     *
     * @param key      关键
     * @param function 函数
     * @return {@link String}
     */
    public String getAndCache(String key, Function<String, String> function) {
        return getAndCacheObj(key, DEFAULT_CACHE_SECONDS, function);
    }

    /**
     * 获取和缓存
     *
     * @param key      关键
     * @param timeOut  缓存超时时间（秒）
     * @param function 函数
     * @return {@link String}
     */
    public String getAndCache(String key, long timeOut, Function<String, String> function){
        return getAndCacheObj(key, timeOut, function);
    }

    /**
     * 获取和缓存（使用二进制的类型）
     *
     * @param key      关键
     * @param timeOut  缓存超时时间（秒）
     * @param function 函数
     * @return {@link String}
     */
    @SuppressWarnings("unchecked")
    public <O> O getAndCacheObj(String key, long timeOut, Function<String, O> function){
        Object o = redisObjTemplate.opsForValue().get(key);
        if (o != null) {
            return (O) o;
        }
        O value = function.apply(key);
        redisObjTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
        return value;
    }

    /* *********************************** Redis分布式锁简单实现 **************************** */

    /**
     * 获得锁
     *
     * 使用注意：
     * 1. 正确的评估执行时间（默认 60S）
     * 2. unLock的时候带入返回的`lockId`标识
     *
     * @param lockKey 锁定键
     * @return lockId锁标识，解锁时使用标识解锁
     */
    public String lock(String lockKey) {
        return lock(lockKey, DEFAULT_LOCK_TIME_OUT);
    }

    /**
     * 获得锁
     *
     * @param lockKey     锁定键
     * @param timeOut     锁超时时间（必须大于1秒！！）
     * @return  lockId锁标识，解锁时使用标识解锁
     */
    public String lock(String lockKey, long timeOut) {
        if (!StringUtils.hasText(lockKey)) {
            throw new ServiceException("lockKey must not be null .");
        }
        assert timeOut > 1000;
        String lockId = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY_PREFIX + lockKey, lockId, timeOut, TimeUnit.MILLISECONDS);
        if (success != null && success) {
            return lockId;
        }
        return null;
    }

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices
     * to improve responsiveness with very short timeouts.
     */
    static final long spinForTimeoutThreshold = 1000000 * 100;
    /**
     * 循环（自旋等待）获取锁
     *
     * @param lockKey     锁定键
     * @param waitTimeout 等待超时    (单位毫秒)
     * @param timeOut     时间        (单位毫秒)
     * @return {@link String}
     */
    public String lockLoop(String lockKey, long waitTimeout, long timeOut) {
        long nanosWaitTimeout = TimeUnit.NANOSECONDS.convert(waitTimeout, TimeUnit.MILLISECONDS);
        final long deadline = System.nanoTime() + nanosWaitTimeout;
        if (waitTimeout <= 0L) {
            return lock(lockKey, timeOut);
        }
        try {
            do { //循环获取锁
                final String lockId = lock(lockKey, timeOut);
                if (lockId != null) {
                    //log.info("获取到锁：" + lockKey);
                    return lockId;
                }
                //log.info("自旋获取锁：" + lockKey);
                //超过100毫秒以上 休眠100ms
                if (deadline - System.nanoTime() > spinForTimeoutThreshold) {
                    Thread.sleep(100);
                }
            } while (System.nanoTime()  < deadline);
        } catch (Throwable e) {
            return null;
        }
        return null;
    }

    /**
     * 解锁，通过锁标识判断是否是由调用者拥有的锁
     *
     * @param lockKey 锁Key
     * @param lockId  锁标识
     */
    public void unLock(String lockKey, String lockId) {
        if (!StringUtils.hasText(lockKey)) {
            return;
        }
        if (!StringUtils.hasText(lockId)) {
            return;
        }
        String currentLockId = redisTemplate.opsForValue().get(LOCK_KEY_PREFIX + lockKey);
        if (currentLockId != null && currentLockId.equals(lockId)) {
            redisTemplate.delete(LOCK_KEY_PREFIX + lockKey);
        }
    }


    /**
     * 执行任务
     *
     * @param key  锁关键词
     * @param time 锁时间（毫秒）
     * @param task 任务
     */
    public void doWork(String key, Long time, Task task) {
        String lockId = null;
        try {
            lockId = lock(key, time);
            if (lockId == null) {
                log.warn("Key:{} 正在处理中...", key);
                throw new ConcurrentException("正在处理中，请稍候...");
            }
            task.run();
        } finally {
            unLock(key, lockId);
        }
    }
    /**
     * 执行任务（有返回值）
     *
     * @param key  锁关键词
     * @param time 锁时间（毫秒）
     * @param callTask 有返回值的任务
     */
    public <T> T doCallWork(String key, Long time, CallTask<T> callTask){
        String lockId = null;
        try {
            lockId = lock(key, time);
            if (lockId == null) {
                log.warn("Key:{} 正在处理中...", key);
                throw new ConcurrentException("正在处理中，请稍候...");
            }
            return callTask.run();
        } finally {
            unLock(key, lockId);
        }
    }

    public interface Task {
        void run() throws RuntimeException;
    }

    public interface CallTask<T> {
        T run() throws RuntimeException;
    }
}
