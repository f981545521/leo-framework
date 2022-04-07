package cn.acyou.leo.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算器
 *
 * @author youfang
 * @version [1.0.0, 2022/4/7 9:22]
 **/
public class Calculator {

    /**
     * 计算器的结果值
     */
    private final BigDecimal result;

    public static Calculator val(Object o1) {
        return new Calculator(o1.toString());
    }

    private Calculator(String v) {
        result = new BigDecimal(v);
    }

    /**
     * 得到结果值
     *
     * @return {@link BigDecimal}
     */
    public BigDecimal getResult() {
        return result;
    }

    /**
     * 加
     *
     * @param o2 o2
     * @return {@link BigDecimal}
     */
    public BigDecimal add(Object o2) {
        return result.add(new BigDecimal(o2.toString()));
    }

    /**
     * 减
     *
     * @param o2 o2
     * @return {@link BigDecimal}
     */
    public BigDecimal subtract(Object o2) {
        return result.subtract(new BigDecimal(o2.toString()));
    }

    /**
     * 乘
     *
     * @param o2 o2
     * @return {@link BigDecimal}
     */
    public BigDecimal multiply(Object o2) {
        return result.multiply(new BigDecimal(o2.toString()));
    }

    /**
     * 除
     *
     * @param o2           o2
     * @param scale        规模
     * @param roundingMode 舍入模式
     * @return {@link BigDecimal}
     */
    public BigDecimal divide(Object o2, int scale, RoundingMode roundingMode) {
        return result.divide(new BigDecimal(o2.toString()), 2, RoundingMode.FLOOR);
    }

    /**
     * 除
     *
     * @param o2 o2
     * @return {@link BigDecimal}
     */
    public BigDecimal divide(Object o2) {
        return divide(o2, 2, RoundingMode.FLOOR);
    }
}
