package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author youfang
 * @version [1.0.0, 2020/11/19]
 **/
@Slf4j
public class NumberUtil {
    /**
     * 单位进位，中文默认为4位即（万、亿）
     */
    public static int UNIT_STEP = 4;

    /**
     * 单位
     */
    public static String[] CN_UNITS = new String[]{"个", "十", "百", "千", "万", "十",
            "百", "千", "亿", "十", "百", "千", "万"};

    /**
     * 汉字
     */
    public static String[] CN_CHARS = new String[]{"零", "一", "二", "三", "四",
            "五", "六", "七", "八", "九"};


    /**
     * 将阿拉伯数字转换为中文数字
     * <p>
     * 123 == 一二三
     *
     * @param srcNum 数字
     * @return 中文数字
     */
    public static String getCnNum(int srcNum) {
        StringBuilder descnnum = new StringBuilder();
        if (srcNum <= 0) {
            descnnum = new StringBuilder("零");
        } else {
            int singleDigit;
            while (srcNum > 0) {
                singleDigit = srcNum % 10;
                descnnum.insert(0, CN_CHARS[singleDigit]);
                srcNum /= 10;
            }
        }
        return descnnum.toString();
    }

    /**
     * 数值转换为中文字符串(口语化)
     *
     * @param num          需要转换的数值  支持(int long double)
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     * @return str
     */
    private static String cvt(String num, boolean isColloquial) {
        int integer, decimal;
        StringBuilder strs = new StringBuilder(32);
        String[] splitNum = num.split("\\.");
        //整数部分
        integer = Integer.parseInt(splitNum[0]);
        //小数部分
        decimal = Integer.parseInt(splitNum[1]);
        String[] integerResult = convert(integer, isColloquial);
        for (String str1 : integerResult) {
            strs.append(str1);
        }
        if (decimal == 0) {
            //小数部分为0时
            return strs.toString();
        } else {
            //例如5.32，小数部分展示三二，而不是三十二
            String decimalResult = getCnNum(decimal);
            strs.append("点");
            strs.append(decimalResult);
            return strs.toString();
        }
    }


    /**
     * 将数值转换为中文
     *
     * @param num          需要转换的数值
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     * @return str[]
     */
    public static String[] convert(long num, boolean isColloquial) {
        // 10以下直接返回对应汉字
        if (num < 10) {
            // ASCII2int
            return new String[]{CN_CHARS[(int) num]};
        }
        char[] chars = String.valueOf(num).toCharArray();
        // 超过单位表示范围的返回空
        if (chars.length > CN_UNITS.length) {
            return new String[]{};
        }
        // 记录上次单位进位
        boolean isLastUnitStep = false;
        // 创建数组，将数字填入单位对应的位置
        ArrayList<String> cnchars = new ArrayList<>(chars.length * 2);
        // 从低位向高位循环
        for (int pos = chars.length - 1; pos >= 0; pos--) {
            char ch = chars[pos];
            // ascii2int 汉字
            String cnChar = CN_CHARS[ch - '0'];
            // 对应的单位坐标
            int unitPos = chars.length - pos - 1;
            // 单位
            String cnUnit = CN_UNITS[unitPos];
            // 是否为0
            boolean isZero = (ch == '0');
            // 是否低位为0
            boolean isZeroLow = (pos + 1 < chars.length && chars[pos + 1] == '0');
            // 当前位是否需要单位进位
            boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));
            // 去除相邻的上一个单位进位
            if (isUnitStep && isLastUnitStep) {
                int size = cnchars.size();
                cnchars.remove(size - 1);
                // 补0
                if (!CN_CHARS[0].equals(cnchars.get(size - 2))) {
                    cnchars.add(CN_CHARS[0]);
                }
            }
            // 单位进位(万、亿)，或者非0时加上单位
            if (isUnitStep || !isZero) {
                cnchars.add(cnUnit);
                isLastUnitStep = isUnitStep;
            }
            // 当前位为0低位为0，或者当前位为0并且为单位进位时进行省略
            if (isZero && (isZeroLow || isUnitStep)) {
                continue;
            }
            cnchars.add(cnChar);
            isLastUnitStep = false;
        }

        Collections.reverse(cnchars);
        // 清除最后一位的0
        int chSize = cnchars.size();
        String chEnd = cnchars.get(chSize - 1);
        if (CN_CHARS[0].equals(chEnd) || CN_UNITS[0].equals(chEnd)) {
            cnchars.remove(chSize - 1);
        }

        // 口语化处理
        if (isColloquial) {
            String chFirst = cnchars.get(0);
            String chSecond = cnchars.get(1);
            // 是否以'一'开头，紧跟'十'
            if (chFirst.equals(CN_CHARS[1]) && chSecond.startsWith(CN_UNITS[1])) {
                cnchars.remove(0);
            }
        }
        return cnchars.toArray(new String[]{});
    }


    public static void main(String[] args) {
        System.out.println(NumberUtil.toUpperCaseAndColloquial(10));
        System.out.println(NumberUtil.toUpperCaseAndColloquial(22));
    }

    /**
     * 大写
     * <p>
     * 口语化。例如12转换为'一十二'而不是'十二'。
     *
     * @param num 数字
     * @return {@link String}
     */
    private static String toUpperCase(int num) {
        return cvt(String.valueOf(num), false);
    }

    /**
     * 大写和口语
     * <p>
     * 口语化。例如12转换为'十二'而不是'一十二'。
     *
     * @param num 数字
     * @return {@link String}
     */
    private static String toUpperCaseAndColloquial(int num) {
        return cvt(String.valueOf(num), true);
    }


    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = toBigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (StringUtils.isNotBlank(value)) {
                result = result.add(toBigDecimal(value));
            }
        }
        return result;
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被减值
     * @return 差
     * @since 4.0.0
     */
    public static BigDecimal sub(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = toBigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (StringUtils.isNotBlank(value)) {
                result = result.subtract(toBigDecimal(value));
            }
        }
        return result;
    }


    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static BigDecimal mul(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = new BigDecimal(values[0]);
        for (int i = 1; i < values.length; i++) {
            result = result.multiply(new BigDecimal(values[i]));
        }

        return result;
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2) {
        return div(toBigDecimal(v1), toBigDecimal(v2), 2, RoundingMode.FLOOR);
    }


    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     * @since 3.0.9
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        Assert.notNull(v2, "Divisor must be not null !");
        if (null == v1) {
            return BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = -scale;
        }
        return v1.divide(v2, scale, roundingMode);
    }

    /**
     * 数字转{@link BigDecimal}<br>
     * null或""或空白符转换为0
     *
     * @param number 数字字符串
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(String number) {
        try {
            number = parseNumber(number).toString();
        } catch (Exception ignore) {
            // 忽略解析错误
        }
        return StringUtils.isBlank(number) ? BigDecimal.ZERO : new BigDecimal(number);
    }


    /**
     * 将指定字符串转换为{@link Number} 对象
     *
     * @param numberStr Number字符串
     * @return Number对象
     * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
     * @since 4.1.15
     */
    public static Number parseNumber(String numberStr) throws NumberFormatException {
        try {
            return NumberFormat.getInstance().parse(numberStr);
        } catch (ParseException e) {
            final NumberFormatException nfe = new NumberFormatException(e.getMessage());
            nfe.initCause(e);
            throw nfe;
        }
    }

    public static Integer parseInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            log.error("转换数字失败：{}", s);
        }
        return null;
    }

    public static Long parseLong(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            log.error("转换数字失败：{}", s);
        }
        return null;
    }
}
