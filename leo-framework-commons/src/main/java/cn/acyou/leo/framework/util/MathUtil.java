package cn.acyou.leo.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalDouble;

/**
 * @author youfang
 * @version [1.0.0, 2020/4/3]
 **/
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
    public static Integer calculationPercent(Integer molecule, Integer denominator) {
        BigDecimal moleculeDecimal = new BigDecimal(molecule);
        BigDecimal denominatorDecimal = new BigDecimal(denominator);
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
        OptionalDouble average = Arrays.stream(sourceNumbers.toArray()).mapToDouble(o -> Double.parseDouble(o.toString())).average();
        if (average.isPresent()) {
            return average.getAsDouble();
        } else {
            throw new IllegalArgumentException("calculation average faild !");
        }
    }

    /**
     * 计算平均数
     *
     * @param sourceNumbers 数据源
     * @return 平均数
     */
    public static Double averageDouble(Object[] sourceNumbers) {
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
     * @param length 长度 <=18
     * @return long
     */
    public static long createMaxLong(int length) {
        if (length == 0) {
            return 0;
        }
        if (length > 18) {
            throw new IllegalArgumentException("length must be less than 18 .");
        }
        return Long.parseLong(StringUtil.concatLengthChar(length, '9'));
    }

    public static void main(String[] args) {
        System.out.println(calculateQy(2, 0));
        System.out.println(calculateQy(3, 2));
        System.out.println(calculateQy(19, 3));
        System.out.println(calculateQy(0, 19));
    }

    /**
     * 计算环比/同比 方法都是一样的，就是上期值不一样
     *
     * QoQ计算环比（quarter on quarter）
     * YoY计算同比（year-over-year）
     *
     * 与历史同时期比较，例如2005年7月份与2004年7月份相比称其为同比；
     * 与上一统计段比较，例如2005年7月份与2005年6月份相比较称其为环比。
     *
     * 环比有环比增长速度和环比发展速度两种方法。
     * 环比即与上期的数量作比较。
     * 环比增长速度=（本期数－上期数）÷上期数×100%，反映本期比上期增长了多少。
     * 环比发展速度=本期数÷上期数×100%，环比发展速度是报告期水平与前一期水平之比，反映现象在前后两期的发展变化情况，如：本期销售额为500万，上期销售额为350万。
     * 环比增长速度=（500－350）÷350×100%=42.86%
     * 环比发展速度=500÷350×100%=142.86%
     * 环比增长速度= 环比发展速度－1。
     *
     * @param currentNum 本期数
     * @param lastNum    上期数
     * @return {@link Integer}
     */
    public static Integer calculateQy(Integer currentNum, Integer lastNum) {
        BigDecimal result = new BigDecimal(currentNum).subtract(new BigDecimal(lastNum));
        if (lastNum == 0){
            return 0;
        }
        BigDecimal multiply = result.divide(new BigDecimal(lastNum), 2, RoundingMode.UP).multiply(HUNDRED);
        return multiply.intValue();
    }

}
