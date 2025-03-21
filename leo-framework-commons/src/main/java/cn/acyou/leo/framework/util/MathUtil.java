package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalDouble;

/**
 * @author youfang
 * @version [1.0.0, 2020/4/3]
 **/
@Slf4j
public class MathUtil {

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    public static final BigDecimal PERCENT = new BigDecimal("0.01");

    /**
     * 计算百分之xx
     * <pre>
     *      calculationPercent(2, 5) = 40
     *      calculationPercent(3, 5) = 60
     * </pre>
     *
     * @param molecule    分子
     * @param denominator 分母
     * @return xx%
     */
    public static Integer calculationPercent(Number molecule, Number denominator) {
        if (denominator.longValue() == 0){
            return 0;
        }
        BigDecimal moleculeDecimal = new BigDecimal(molecule.longValue());
        BigDecimal denominatorDecimal = new BigDecimal(denominator.longValue());
        BigDecimal divide = moleculeDecimal.divide(denominatorDecimal, 2, RoundingMode.HALF_UP);
        return divide.multiply(HUNDRED).intValue();
    }

    /**
     * 计算平均数
     *
     * @param sourceNumbers 数据源
     * @return 平均数
     */
    public static Double averageDouble(Collection<Object> sourceNumbers) {
        try {
            OptionalDouble average = Arrays.stream(sourceNumbers.toArray()).mapToDouble(o -> Double.parseDouble(o.toString())).average();
            if (average.isPresent()) {
                return average.getAsDouble();
            }
        }catch (Exception e) {
            log.warn("计算平均数失败 {} | {}", e.getMessage(), Arrays.toString(sourceNumbers.toArray()));
        }
        return null;
    }

    /**
     * 计算平均数
     *
     * @param sourceNumbers 数据源
     * @return 平均数
     */
    public static Double averageDouble(Object... sourceNumbers) {
        return averageDouble(Arrays.asList(sourceNumbers));
    }

    /**
     * 创建指定长度的最大long类型
     * <p>
     * Long.MAX_VALUE: 9223372036854775807
     * <pre>
     * createMaxLong(4)     = 9999
     * createMaxLong(6)     = 999999
     * createMaxLong(8)     = 99999999
     * </pre>
     * @param length 长度 小于=18
     * @return long
     */
    public static long createMaxLong(int length) {
        if (length == 0) {
            return 0;
        }
        if (length > 18) {
            throw new IllegalArgumentException("length must be less than 18 .");
        }
        return Long.parseLong(StringUtils.concatLengthChar(length, '9'));
    }


    /**
     * 计算环比/同比 方法都是一样的，就是上期值不一样
     *
     * <ul>
     *     <li>QoQ计算环比（quarter on quarter）</li>
     *     <li>YoY计算同比（year-over-year）</li>
     * </ul>
     *
     * <p>与历史同时期比较，例如2005年7月份与2004年7月份相比称其为同比；</p>
     * <p>与上一统计段比较，例如2005年7月份与2005年6月份相比较称其为环比。</p>
     *
     * <p>环比有环比增长速度和环比发展速度两种方法。</p>
     * <p>环比即与上期的数量作比较。</p>
     * <p>环比增长速度=（本期数－上期数）÷上期数×100%，反映本期比上期增长了多少。</p>
     * <p>环比发展速度=本期数÷上期数×100%，环比发展速度是报告期水平与前一期水平之比，反映现象在前后两期的发展变化情况，如：本期销售额为500万，上期销售额为350万。</p>
     * <p>环比增长速度=（500－350）÷350×100%=42.86%</p>
     * <p>环比发展速度=500÷350×100%=142.86%</p>
     * <p>环比增长速度= 环比发展速度－1。</p>
     *
     * @param lastNum 上期数
     * @return {@link Integer}
     */
    public static Integer calculateQy(Integer currentNum, Integer lastNum) {
        BigDecimal result = new BigDecimal(currentNum).subtract(new BigDecimal(lastNum));
        if (lastNum == 0) {
            return 0;
        }
        BigDecimal multiply = result.divide(new BigDecimal(lastNum), 2, RoundingMode.UP).multiply(HUNDRED);
        return multiply.intValue();
    }

    /**
     * 求最大公约数
     *
     * @param M 值1
     * @param N 值2
     * @return 最大公约数
     */
    private static int commonDivisor(int M, int N) {
        M = Math.abs(M);
        N = Math.abs(N);
        if (N == 0) {
            return M;
        }
        return commonDivisor(N, M % N);
    }

    /**
     * 根据像素 计算屏幕比例（分辨率）
     * <pre>
     * System.out.println(proportion(1920, 1080));//16:9
     * System.out.println(proportion(3000, 2000));//3:2
     * System.out.println(commonDivisor(3840, 0));//3840
     * System.out.println(commonDivisor(0, 2160));//2160
     * System.out.println(proportion(3840, 0));//1:0
     * System.out.println(proportion(0, 2160));//0:1
     * System.out.println(proportion(3840, -2160));//16:-9
     * System.out.println(proportion(-3840, 2160));//-16:9
     * </pre>
     *
     * @param x x
     * @param y y
     * @return {@link String}
     */
    public static String proportion(int x, int y) {
        int i = commonDivisor(x, y);
        return x / i + ":" + y / i;
    }

}
