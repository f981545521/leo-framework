package cn.acyou.leo.pay;


import cn.acyou.leo.framework.util.Calculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author youfang
 * @version [1.0.0, 2022-4-7]
 **/
public class MainTest1 {
    public static void main(String[] args) {
        final BigDecimal conversion = Calculator.conversion("12*0.0001");
        System.out.println(conversion);
    }

    @Test
    public void test24() {
        System.out.println(Calculator.val(0.2346).gt(0.2345));
        System.out.println(Calculator.val(0.2345).gte(0.2345));
        System.out.println(Calculator.val(0.2345).eq(0.2345));
        System.out.println(Calculator.val(0.2345).lt(0.2345));
        System.out.println(Calculator.val(0.2345).lte(0.2345));
        System.out.println(Calculator.val(0.234));
        System.out.println(new BigDecimal(0.234));
        BigDecimal divide = Calculator.val(10).divide(3, 10, RoundingMode.FLOOR);
        //BigDecimal divide = Calculator.val(10).divide(3);
        //java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
        System.out.println(divide);
    }
}
