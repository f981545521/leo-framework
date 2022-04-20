package cn.acyou.leo.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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
    private BigDecimal result;

    public static Calculator val(Object o1) {
        return new Calculator(o1.toString());
    }

    private Calculator(String v) {
        result = new BigDecimal(v);
    }

    /**
     * 大于
     *
     * @param o2 o2
     * @return boolean
     */
    public boolean gt(Object o2) {
        return result.compareTo(new BigDecimal(o2.toString())) > 0;
    }

    /**
     * 大于或者等于
     *
     * @param o2 o2
     * @return boolean
     */
    public boolean gte(Object o2) {
        return result.compareTo(new BigDecimal(o2.toString())) >= 0;
    }

    /**
     * 小于
     *
     * @param o2 o2
     * @return boolean
     */
    public boolean lt(Object o2) {
        return result.compareTo(new BigDecimal(o2.toString())) < 0;
    }

    /**
     * 小于或者等于
     *
     * @param o2 o2
     * @return boolean
     */
    public boolean lte(Object o2) {
        return result.compareTo(new BigDecimal(o2.toString())) <= 0;
    }

    /**
     * 等于
     *
     * @param o2 o2
     * @return boolean
     */
    public boolean eq(Object o2) {
        return result.compareTo(new BigDecimal(o2.toString())) == 0;
    }

    /**
     * 取平均值
     *
     * @param values 值列表
     * @return {@link BigDecimal}
     */
    public static BigDecimal avg(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = new BigDecimal("0");
        for (BigDecimal val : values) {
            total = total.add(val);
        }
        return total.divide(new BigDecimal(values.size()));
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


    @Override
    public String toString() {
        return "Calculator{" +
                "result=" + result +
                '}';
    }

    /**
     * 计算表达式的值
     *
     * @param expression 表达式
     * @return 计算结果
     */
    public static BigDecimal conversion(String expression) {
        final Calculator cal = new Calculator("0");
        expression = expression.replaceAll(" ", "");
        expression = transform(expression);
        return cal.calculate(expression);
    }

    private final Stack<String> postfixStack = new Stack<>();// 后缀式栈
    private final Stack<Character> opStack = new Stack<>();// 运算符栈
    private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};// 运用运算符ASCII码-40做索引的运算符优先级

    /**
     * 按照给定的表达式计算
     *
     * @param expression 要计算的表达式例如:5+12*(3+5)/7
     * @return 计算结果
     */
    public BigDecimal calculate(String expression) {
        Stack<String> resultStack = new Stack<>();
        prepare(expression);
        Collections.reverse(postfixStack);// 将后缀式栈反转
        String firstValue, secondValue, currentValue;// 参与计算的第一个值，第二个值和算术运算符
        while (false == postfixStack.isEmpty()) {
            currentValue = postfixStack.pop();
            if (false == isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                secondValue = resultStack.pop();
                firstValue = resultStack.pop();

                // 将负数标记符改为负号
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                BigDecimal tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult.toString());
            }
        }
        result = new BigDecimal(resultStack.pop());
        return result;
    }

    /**
     * 判断是否为算术符号
     *
     * @param c 字符
     * @return 是否为算术符号
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    /**
     * 利用ASCII码-40做下标去算术符号优先级
     *
     * @param cur  下标
     * @param peek peek
     * @return 优先级
     */
    public boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
            result = true;
        }
        return result;
    }

    /**
     * 数据准备阶段将表达式转换成为后缀式栈
     *
     * @param expression 表达式
     */
    private void prepare(String expression) {
        opStack.push(',');// 运算符放入栈底元素逗号，此符号优先级最低
        char[] arr = expression.toCharArray();
        int currentIndex = 0;// 当前字符的位置
        int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        char currentOp, peekOp;// 当前操作符和栈顶操作符
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (isOperator(currentOp)) {// 如果当前字符是运算符
                if (count > 0) {
                    postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }

    /**
     * 将表达式中负数的符号更改
     *
     * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
     * @return 转换后的字符串
     */
    private static String transform(String expression) {
        final char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if (arr[0] == '~' || (arr.length > 1 && arr[1] == '(')) {
            arr[0] = '-';
            return "0" + new String(arr);
        } else {
            return new String(arr);
        }
    }


    /**
     * 按照给定的算术运算符做计算
     *
     * @param firstValue  第一个值
     * @param secondValue 第二个值
     * @param currentOp   算数符，只支持'+'、'-'、'*'、'/'
     * @return 结果
     */
    private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
        BigDecimal result;
        switch (currentOp) {
            case '+':
                result = NumberUtil.add(firstValue, secondValue);
                break;
            case '-':
                result = NumberUtil.sub(firstValue, secondValue);
                break;
            case '*':
                result = NumberUtil.mul(firstValue, secondValue);
                break;
            case '/':
                result = NumberUtil.div(firstValue, secondValue);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentOp);
        }
        return result;
    }
}
