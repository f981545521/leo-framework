package cn.acyou.leo.pay;


import cn.acyou.leo.framework.util.Calculator;

import java.math.BigDecimal;

/**
 * @author youfang
 * @version [1.0.0, 2022-4-7]
 **/
public class MainTest1 {
    public static void main(String[] args) {
        final BigDecimal conversion = Calculator.conversion("12*0.0001");
        System.out.println(conversion);
    }
}
