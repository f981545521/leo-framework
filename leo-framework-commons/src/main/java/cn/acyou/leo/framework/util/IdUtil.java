package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.commons.SnowFlake;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 统一的ID生成工具
 *
 * @author youfang
 * @version [1.0.0, 2020-7-24 下午 08:04]
 **/
@Component
public class IdUtil {
    private static RedisUtils redisUtils;
    private static SnowFlake snowFlake;

    private IdUtil(){

    }

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        IdUtil.redisUtils = redisUtils;
    }

    @Autowired
    public void setSnowFlake(SnowFlake snowFlake) {
        IdUtil.snowFlake = snowFlake;
    }

    /**
     * 雪花算法 nextId
     *
     * @return 字符串
     */
    public static long nextId() {
        return snowFlake.nextId();
    }

    /**
     * 雪花算法 nextIdStr
     *
     * @return 字符串
     */
    public static String nextIdStr() {
        return snowFlake.nextIdStr();
    }
    /**
     * 雪花算法 nextIdPrefix
     *
     * @return 字符串
     */
    public static String nextIdPrefix(String prefix) {
        return snowFlake.nextIdPrefix(prefix);
    }

    /**
     * UUID的字符串
     *
     * @return 字符串
     */
    public static String uuidStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 没有-的UUID字符串
     *
     * @return 字符串
     */
    public static String uuidStrWithoutLine() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 创建MongoDB ID生成策略实现<br>
     * ObjectId由以下几部分组成：
     *
     * <pre>
     * 1. Time 时间戳。
     * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
     * 3. PID 进程ID。确保同一机器中不冲突
     * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
     * </pre>
     * <p>
     * 参考：http://blog.csdn.net/qxc1281/article/details/54021882
     *
     * @return ObjectId
     */
    public static String objectId() {
        return ObjectId.next();
    }

    /**
     * 公共获取单号
     * <pre>
     *  getDatePrefix4BitId("RK")    =   RK2020072700001
     * </pre>
     *
     * @param prefix 前缀
     * @return 单号
     */
    public static String getDatePrefixReceiptNo(String prefix) {
        return getDatePrefixId(prefix, 5);
    }

    /**
     * 从Redis中获取ID
     * <p>
     * prefix + 20200724 + 00001
     *
     * @param prefix 前缀
     * @param length 长度
     * @return ID
     */
    public static String getDatePrefixId(String prefix, int length) {
        String formatDate = DateUtil.getCurrentDateShortFormat();
        String key = "SEQ:" + formatDate;
        //KEY:  SEQ:20200724                timeOut：36小时(36 * 60 * 60)，销毁一天半之前的Key
        Long increment = redisUtils.increment(key, 1, 129600);
        long maxV = MathUtil.createMaxLong(length);
        if (increment > maxV) {
            throw new ServiceException("ID获取错误：超出最大值!");
        }
        DecimalFormat df = new DecimalFormat(StringUtil.concatLengthChar(length, '0'));
        return prefix + formatDate + df.format(increment);
    }

}
