package cn.acyou.leo.framework.util.redis;

import cn.acyou.leo.framework.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 命令参考
 * http://doc.redisfans.com/
 *
 * @author youfang
 * @version [1.0.0, 2020-3-21 下午 10:44]
 **/
@Component
public class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取String 类型
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        String strValue = redisTemplate.opsForValue().get(key);
        if (log.isDebugEnabled()){
            log.debug("接口调用详情：参数K： " + key);
        }
        return strValue;
    }

    /**
     * 设置String 类型
     *
     * @param key     K
     * @param value   V
     * @param timeout 超时
     * @param unit    单位
     */
    public void set(String key, String value, Long timeout, TimeUnit unit) {
        if (log.isDebugEnabled()){
            log.debug("{}|{}|{}|{}|{}", "set方法入参：", "键:" + key, "值:" + value, "存活时间:" + timeout, "时间单位:" + unit);
        }
        if (timeout != null) {
            redisTemplate.opsForValue().set(key, value, timeout, unit != null ? unit : TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 设置String 类型
     *
     * @param key     K
     * @param value   V
     * @param timeout 超时时间（单位秒）
     */
    public void set(String key, String value, long timeout) {
        if (log.isDebugEnabled()){
            log.debug("接口调用详情：参数K-V： " + key + "=" + value);
        }
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置String 类型 (永久有效)
     *
     * @param key   K
     * @param value V
     */
    public void set(String key, String value) {
        if (log.isDebugEnabled()){
            log.debug("接口调用详情：参数K-V： " + key + "=" + value);
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据key删除存在的Key
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据多个key批量删除缓存中的value
     *
     * @param keys keys
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     *
     * @param key     key
     * @param timeout 出栈操作的连接阻塞保护时间,时间单位为秒
     * @return v
     */
    public String listLeftPop(String key, long timeout) {
        if (log.isDebugEnabled()){
            log.debug("接口调用详情：参数K： " + key);
        }
        return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     *
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     *
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long listLeftPushAll(String key, Collection<String> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 移除并返回列表 key 的尾元素。
     *
     * @param key     key
     * @param timeout 出队操作的连接阻塞保护时间,时间单位为秒
     * @return v
     */
    public String listRightPop(String key, long timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 移除并返回列表 key 的尾元素。
     *
     * @param key key
     * @return 列表的尾元素。
     */
    public String listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除并返回列表 key 的尾元素。 并转为指定对象
     *
     * @param key   Key
     * @param clazz clazz
     * @return {@link T}
     */
    public <T> T listRightPop2Object(String key, Class<T> clazz) {
        try {
            String strValue = redisTemplate.opsForList().rightPop(key);
            if (StringUtils.isNotEmpty(strValue)) {
                return JSON.parseObject(strValue, clazz);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Json转对象失败!", e);
            return null;
        }
    }

    /**
     * 将一个 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key 键
     * @return 执行 RPUSH 操作后，表的长度。
     */
    public Long listRightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key 键
     * @return 执行 RPUSH 操作后，表的长度。
     */
    public Long listRightPushAll(String key, Collection<String> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 移除并返回集合中的一个随机元素。
     *
     * @param key 键
     * @return 被移除的随机元素。
     * 当 key 不存在或 key 是空集时，返回 nil 。
     */
    public String setPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 对Set的添加操作
     *
     * @param key    键
     * @param values 插入Set的String数组
     * @return Long
     */
    public Long setAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 移除集合key中的一个或多个member元素，不存在的member元素会被忽略。
     *
     * @param key    键
     * @param values 值
     */
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机获取多个key无序集合中的元素（去重），count表示个数
     *
     * @param key   键
     * @param count 数量
     * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil 。
     * 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。
     */
    public Set<String> setDistinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 根据key获取  hashKey对应的value的值 并转换为Bean
     *
     * @param key     键
     * @param hashKey hashKey
     * @return Bean
     */
    public <T> T hashGet(String key, String hashKey, Class<T> clazz) {
        String o = (String) redisTemplate.opsForHash().get(key, hashKey);
        return JSON.parseObject(o, clazz);
    }

    /**
     * 根据key获取  hashKey对应的value的List
     *
     * @param key 键
     * @return hashKey对应的value的List
     */
    public List<Object> hashMultiGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    /**
     * 返回键上的所有hashKey与value
     *
     * @param key 键
     * @return 键上所有的hashKey与value
     */
    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 根据hashkey向key对应的HashMap中添加value
     *
     * @param key     键
     * @param hashKey 对应的HashMap中的Key
     * @param value   值
     */
    public void hashPut(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, JSON.toJSONString(value));
    }

    /**
     * 根据hashkey向key对应的HashMap中添加value
     *
     * @param key     键
     * @param hashKey 对应的HashMap中的Key
     * @param value   值
     */
    public void hashPut(String key, Object hashKey, Object value, long outTime) {
        redisTemplate.opsForHash().put(key, hashKey, JSON.toJSONString(value));
    }

    /**
     * 根据key向缓存中插入整个HashMap
     *
     * @param key 键
     * @param map 要插入的HashMap对象
     */
    public void hashPutAll(String key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key      键
     * @param hashKeys hash值
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    public Long hashDelete(String key, String... hashKeys) {
        Long counts;
        if (hashKeys.length > 1) {
            Object[] objKeys = hashKeys.clone();
            counts = redisTemplate.opsForHash().delete(key, objKeys);
        } else {
            counts = redisTemplate.opsForHash().delete(key, hashKeys[0]);
        }
        if (log.isDebugEnabled()) {
            log.debug("接口调用详情：参数K： " + key);
        }
        return counts;
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey hash值 不能为null
     * @return true 存在 false不存在
     */
    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 根据key获取Map中所有的记录条数
     *
     * @param key 键
     * @return 返回哈希表 key 中域的数量。
     */
    public Long hashSize(String key) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     * <p>
     * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
     * score 值可以是整数值或双精度浮点数。
     * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     *
     * @param key   key
     * @param value value
     * @param score 分数
     * @return true/false
     */
    public Boolean zSetAdd(String key, String value, double score) {
        log.debug("接口调用详情：参数K： " + key + "值： " + value + "");
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 根据分数从大到小 获取前几个
     *
     * @param key   key
     * @param limit 前几
     * @return value集合
     */
    public Set<String> zSetReverseRangeLimit(String key, long limit) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().reverseRange(key, 0, limit);
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
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     *
     * @param key    key 键
     * @param values values 值
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zSetRemove(String key, Object... values) {
        log.debug("接口调用详情：参数K： " + key + "值： " + Arrays.toString(values) + "");
        return redisTemplate.opsForZSet().remove(key, values);
    }


    /**
     * 返回一个成员范围的有序集合，通过索引，以分数排序，从低分到高分
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return value 集合
     */
    public Set<String> zSetRange(String key, long start, long end) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 返回一个成员范围的有序集合（由字典范围）
     *
     * @param key   key
     * @param range range
     * @param limit limit
     * @return 范围的Value集合
     */
    public Set<String> zSetRangeByLex(String key, Range range, Limit limit) {
        log.debug("接口调用详情：参数K： " + key);
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
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
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
        log.debug("接口调用详情：参数K： " + key);
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
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 根据分数获取value的排名
     * <p>
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
     * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
     * 使用 ZRANK 命令可以获得成员按 score 值递增(从小到大)排列的排名。
     *
     * @param key    键
     * @param member value
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名。
     * 如果 member 不是有序集 key 的成员，返回 nil 。
     */
    public Long zSetReverseRank(String key, String member) {
        if (log.isDebugEnabled()) {
            log.debug("接口调用详情：参数K： " + key);
        }
        return redisTemplate.opsForZSet().reverseRank(key, member);
    }

    /**
     * 返回有序集key中成员member的排名。
     * 其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score值最小的成员排名为0。
     * 使用ZREVRANK命令可以获得成员按score值递减(从大到小)排列的排名。
     *
     * @param key    键
     * @param member value
     * @return 如果member是有序集key的成员，返回integer-reply：member的排名。
     * 如果member不是有序集key的成员，返回bulk-string-reply: nil。
     */
    public Long zSetRank(String key, String member) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().rank(key, member);
    }

    /**
     * 返回有序集key中，成员member的score值。
     * 如果member元素不是有序集key的成员，或key不存在，返回nil。
     *
     * @param key    键
     * @param member value
     * @return member成员的score值（double型浮点数）
     */
    public Double zSetScore(String key, String member) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().score(key, member);
    }

    /**
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
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().incrementScore(key, member, deal);
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * 其中成员的位置按 score 值递增(从小到大)来排序。
     * <p>
     * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE {@link RedisUtils#zSetReverseRangeWithScores}命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * <p>
     * 超出范围的下标并不会引起错误。
     * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
     * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 成员和它的 score 值 集合
     */
    public List<ZSetItem> zSetRangeWithScores(String key, long start, long end) {
        if (log.isDebugEnabled()) {
            log.debug("接口调用详情：参数K： " + key);
        }
        Set<TypedTuple<String>> sset = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        return buildZsetList(Objects.requireNonNull(sset));
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     * <p>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，
     * 注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
     *
     * @param key    键
     * @param min    最小值
     * @param max    最大值
     * @param offset 偏移
     * @param count  数量
     * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
     */
    public List<ZSetItem> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        log.debug("接口调用详情：参数K： " + key + ",min:" + min + ",max:" + max + ",offset:" + offset + ",count:" + count);
        Set<TypedTuple<String>> sset = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
        if (sset == null){
            return new ArrayList<>();
        }
        return buildZsetList(sset);
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * 其中成员的位置按 score 值递增(从大到小)来排序。
     * 参考； {@link RedisUtils#zSetRangeWithScores}
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 成员和它的 score 值 集合
     */
    public List<ZSetItem> zSetReverseRangeWithScores(String key, long start, long end) {
        if (log.isDebugEnabled()) {
            log.debug("接口调用详情：参数K： " + key);
        }
        Set<TypedTuple<String>> sset = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (sset == null){
            return new ArrayList<>();
        }
        return buildZsetList(sset);
    }

    /**
     * 判定当前key是否存在
     *
     * @param key 键
     * @return true/false
     */
    public Boolean hasKey(String key) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.hasKey(key);
    }

    /**
     * 返回有序集 key 的基数。
     *
     * @param key 键
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。
     * 当 key 不存在时，返回 0 。
     */
    public Long zSetZCard(String key) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 返回列表key的长度。
     * <p>
     * 如果key不存在，则key被解释为一个空列表，返回0. 如果key不是列表类型，返回一个错误。
     *
     * @param key 键
     * @return 列表 key 的大小。
     */
    public Long listSize(String key) {
        log.debug("接口调用详情：参数K： " + key);
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * <p>
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 一个列表，包含指定区间内的元素。
     */
    public List<String> listRange(String key, long start, long end) {
        if (log.isDebugEnabled()) {
            log.debug("接口调用详情：参数K： " + key);
        }
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 列表的范围查询和删除
     * 注意： list的下标为0开始，start 开始为0 ， **end 是下标不是个数！**
     *
     *
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return {@link List<String>}
     */
    public List<String> listRangeAndRemove(String key, long start, long end){

        SessionCallback<List<String>> sessionCallback = new SessionCallback<List<String>>() {
            @Override
            public List<String> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTemplate.opsForList().range(key, start, end);
                redisTemplate.opsForList().trim(key, end + 1, -1);
                List<?> exec = operations.exec();
                List<String> lrangeList = (List<String>) exec.get(0);
                return lrangeList;
            }
        };
        return redisTemplate.execute(sessionCallback);
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时时间
     * @return true/false
     */
    public Boolean setNx(String key, String value, long timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 使用规则删除匹配的键
     *
     * @param pattern 模式
     * @return {@link Set<String>}
     */
    public Set<String> deleteKeysInPattern(String pattern) {
        log.debug("{}|{}|{}", "获取所有匹配pattern参数的Keys", "[KEYS pattern]:", pattern);
        Set<String> keySets = redisTemplate.keys(pattern);
        if (CollectionUtils.isNotEmpty(keySets)){
            redisTemplate.delete(keySets);
        }
        return keySets;
    }

    /**
     * 封装Zset 查询结果
     *
     * @param zset zset
     * @return List<ZSetItem>
     */
    private List<ZSetItem> buildZsetList(Set<TypedTuple<String>> zset) {
        List<ZSetItem> result = new ArrayList<>();
        for (TypedTuple<String> stringTypedTuple : zset) {
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
     * 自增 / 自减
     *
     * @param key   key
     * @param delta 1自增1 -1减少1
     * @return 执行 INCR 命令之后 key 的值。
     */
    public Long increment(String key, long delta) {
        log.debug("{}|{}|{}", "increment接口开始调用：", "key:" + key, "delta:" + delta);
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 获取过期时间（秒）
     *
     * @param key key
     * @return {@link Long}
     */
    public Long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取过期时间（指定单位）
     *
     * @param key key
     * @return {@link Long}
     */
    public Long getExpire(String key, TimeUnit timeUnit){
        return redisTemplate.getExpire(key, timeUnit);
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
        log.debug("{}|{}|{}|{}", "increment And Expire 接口开始调用：", "key:" + key, "delta:" + delta, "timeOut:" + timeOut);
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
     * 指定过期
     *
     * @param key      Key
     * @param timeout  超时
     * @param timeUnit 时间单位
     * @return {@link Boolean}
     */
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        log.debug("{}|{}|{}|{}", "expire接口开始调用：", "key:" + key, "timeout:" + timeout, "timeUnit:" + timeUnit);
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 返回集合 key 中的所有成员。
     *
     * @param key Key
     * @return {@link Set<String>}
     */
    public Set<String> getMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     *
     * @param key         key
     * @param typedTuples 输入元组
     * @return {@link Long}
     */
    public Long zSetAdd(String key, Set<TypedTuple<String>> typedTuples) {
        return redisTemplate.opsForZSet().add(key, typedTuples);
    }

    /**
     * z分数设置删除范围
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     * @return {@link Long}
     */
    public Long zSetRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return {@link Long}
     */
    public Long zSetRemoveRange(String key, Long start, Long end) {
        long startValue = start != null ? start : 0;
        long endValue = end != null ? end : -1;
        return redisTemplate.opsForZSet().removeRange(key, startValue, endValue);
    }

    /**
     * 根据表达式查询所有key
     *
     * @return key Set
     */
    public Set<String> keys(String pattern) {
        log.debug("接口调用详情：参数K： " + pattern);
        return redisTemplate.keys(pattern);

    }

    /* ***********************************Redis分布式锁**************************** */

    private static final String LOCK_KEY_PREFIX = "REDIS_LOCK:";
    /**
     * 默认超时时间 60S
     */
    private static final int DEFAULT_TIME_OUT = 60 * 1000;

    /**
     * 获得锁
     *
     * @param lockKey 锁定键
     * @return {@link String} lockId锁标识，解锁时使用标识解锁
     */
    public String lock(String lockKey) {
        return lock(lockKey, DEFAULT_TIME_OUT);
    }

    /**
     * 获得锁
     *
     * @param lockKey     锁定键
     * @param milliSecond 毫秒
     * @return {@link String} lockId锁标识，解锁时使用标识解锁
     */
    public String lock(String lockKey, long milliSecond) {
        if (StringUtils.isEmpty(lockKey)){
            throw new ServiceException("lockKey must not be null .");
        }
        assert milliSecond > 1000;
        String lockId = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY_PREFIX + lockKey, lockId, milliSecond, TimeUnit.MILLISECONDS);
        if (success != null && success){
            return lockId;
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
        if (StringUtils.isEmpty(lockKey)){
            return;
        }
        if (StringUtils.isEmpty(lockId)){
            return;
        }
        String currentLockId = redisTemplate.opsForValue().get(LOCK_KEY_PREFIX + lockKey);
        if (currentLockId != null && currentLockId.equals(lockId)){
            redisTemplate.delete(LOCK_KEY_PREFIX + lockKey);
        }
    }

}
