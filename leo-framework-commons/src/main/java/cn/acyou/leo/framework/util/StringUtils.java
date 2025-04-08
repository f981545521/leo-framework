package cn.acyou.leo.framework.util;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 补充 Spring String 相关方法
 *
 * @author youfang
 * @version [1.0.0, 2020-7-24 下午 09:31]
 **/
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String NEW_LINE = "\r\n";

    /**
     * 拼接重复字符到指定长度
     *
     * <pre>
     * StringUtil.concatLengthChar(4, '9')     = 9999
     * StringUtil.concatLengthChar(4, '0')     = 0000
     * StringUtil.concatLengthChar(5, '8')     = 88888
     * </pre>
     *
     * @param length    长度
     * @param character 字符
     * @return {@link String}
     */
    public static String concatLengthChar(int length, char character) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(character);
        }
        return sb.toString();
    }


    /**
     * 字符串加入逗号
     *
     * @param iterable 可迭代的
     * @return {@link String}
     */
    public static String joinOnComma(final Iterable<?> iterable) {
        return org.apache.commons.lang3.StringUtils.join(iterable.iterator(), COMMA);
    }

    /**
     * 格式化模板
     * <pre>
     * String templateStr= "{姓名}今年{岁}啦！";
     * System.out.println(formatTemplate(templateStr, "王二小", "3"));//王二小今年3啦！
     * </pre>
     *
     * @param templateStr 模板字符串
     * @param params   参数列表
     * @return 格式化后内容
     */
    public static String formatTemplate(String templateStr, String... params) {
        List<String> matchStr = RegexUtil.getMatchStr(templateStr, "\\{(.+?)\\}");
        Map<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < matchStr.size(); i++) {
            String varStr = matchStr.get(i);
            paramsMap.put(varStr.substring(1, varStr.length() - 1), params[i]);
        }
        return formatTemplate(templateStr, paramsMap);
    }


    /**
     * 格式化模板
     * <pre>
     *         String templateStr= "{姓名}今年{岁}啦！";
     *         Map<String, String> paramMap = new HashMap<>();
     *         paramMap.put("姓名", "王二小");
     *         paramMap.put("岁", "3");
     *         System.out.println(formatTemplate(templateStr, paramMap));//王二小今年3啦！
     *
     * </pre>
     *
     * @param templateStr 模板字符串
     * @param paramsMap   参数
     * @return 格式化后内容
     */
    public static String formatTemplate(String templateStr, Map<String, String> paramsMap) {
        String templateContent = templateStr;
        Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getValue() == null) {
                continue;
            }
            if (templateContent.indexOf(entry.getKey()) > 0) {
                templateContent = templateContent.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return templateContent;
    }

    /**
     * 转换字符串
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String toStr(Object obj) {
        String str = "";
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    /**
     * 返回文字内容，如果是null -> "-"
     *
     * @param str str
     * @return {@link String}
     */
    public static String nullAsDashed(String str) {
        if (str == null) {
            return "-";
        }
        return str;
    }

    /**
     * 如果一个对象为空则返回另外一个对象
     *
     * @param value        对象
     * @param defaultValue 对象为空时返回
     * @return {@link Object}
     */
    public static String isBlank(Object value, Object defaultValue) {
        if (isBlank(value)) {
            return defaultValue.toString();
        }
        return value.toString();
    }

    /**
     * 不是null或空字符串
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNotBlank(Object obj) {
        return obj != null && isNotBlank(obj.toString());
    }

    /**
     * 不是null或空字符串 返回默认值
     *
     * @param obj          obj
     * @param defaultValue 默认值
     * @return {@link String}
     */
    public static String isNotBlank(Object obj, Object defaultValue) {
        return isNotBlank(obj) ? obj.toString() : defaultValue.toString();
    }

    /**
     * 是null或空字符串
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isBlank(Object obj) {
        return obj == null || isBlank(obj.toString());
    }

    /**
     * str长度分割
     *
     * @param sourceStr 源str
     * @param length    长度 (几个汉字)
     * @return str array
     */
    public static String[] strLengthSplit(String sourceStr, int length) {
        if (isBlank(sourceStr)) {
            return new String[]{"", ""};
        }
        int sp = length * 2;
        int indexCount = 0;
        int index = 0;
        char[] chars = sourceStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (RegexUtil.isChinese(c)) {
                indexCount += 2;
            } else {
                indexCount++;
            }
            if (indexCount > sp) {
                index = i;
                break;
            }
        }
        String[] returnStr = new String[]{"", ""};
        if (index > 0) {
            returnStr[0] = sourceStr.substring(0, index);
            returnStr[1] = sourceStr.substring(index);
        } else {
            returnStr[0] = sourceStr;
            returnStr[1] = "";
        }
        return returnStr;
    }

    /**
     * 判断是否 http 地址
     *
     * @param text 文本
     * @return 是否 http 地址
     */
    public static boolean isHttpUrl(String text) {
        return text.startsWith("http://") || text.startsWith("https://");
    }

    /**
     * 替换指定字符串的指定区间内字符为"*"
     * 俗称：脱敏功能，后面其他功能，可以见：DesensitizedUtils(脱敏工具类)
     *
     * <pre>
     * StrUtil.hide(null,*,*)=null
     * StrUtil.hide("",0,*)=""
     * StrUtil.hide("jackduan@163.com",-1,4)   ****duan@163.com
     * StrUtil.hide("jackduan@163.com",2,3)    ja*kduan@163.com
     * StrUtil.hide("jackduan@163.com",3,2)    jackduan@163.com
     * StrUtil.hide("jackduan@163.com",16,16)  jackduan@163.com
     * StrUtil.hide("jackduan@163.com",16,17)  jackduan@163.com
     * </pre>
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @return 替换后的字符串
     * @since 4.1.14
     */
    public static String hide(CharSequence str, int startInclude, int endExclude) {
        return replace(str, startInclude, endExclude, '*');
    }
    /**
     * 替换指定字符串的指定区间内字符为固定字符
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @param replacedChar 被替换的字符
     * @return 替换后的字符串
     * @since 3.2.1
     */
    public static String replace(CharSequence str, int startInclude, int endExclude, char replacedChar) {
        if (str == null || str.length() == 0) {
            return str(str);
        }
        final int strLength = str.length();
        if (startInclude > strLength) {
            return str(str);
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            // 如果起始位置大于结束位置，不替换
            return str(str);
        }

        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++) {
            if (i >= startInclude && i < endExclude) {
                chars[i] = replacedChar;
            } else {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }
    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param sourStr  原始字符串
     * @param length   指定长度
     * @return 指定长度的字符串列表
     */
    public static List<String> splitByLength(String sourStr, int length) {
        return splitByLength(new ArrayList<>(), sourStr, length);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param container     容器
     * @param sourStr       字符串
     * @param length        长度
     * @return 指定长度的字符串列表
     */
    private static List<String> splitByLength(List<String> container, String sourStr, int length){
        if (sourStr != null && sourStr.length() > 0) {
            //源字符串的长度小于分割的长度
            if (sourStr.length() < length) {
                container.add(sourStr);
                return container;
            }
            String str0 = sourStr.substring(0, length);
            String str1 = sourStr.substring(length);
            container.add(str0);
            return splitByLength(container, str1, length);
        }
        return container;
    }

    /**
     * 使用System.out.println 打印任何对象
     * @param obj 任意对象
     */
    @SuppressWarnings("unchecked")
    public static void println(Object obj){
        if (obj == null) {
            System.out.println("null");
            return;
        }
        if (obj instanceof CharSequence) {
            System.out.println(obj);
            return;
        }
        if (obj.getClass().isArray()) {
            Arrays.stream((Object[]) obj).forEach(System.out::println);
            return;
        }
        if (obj instanceof Collection) {
            ((Collection<Object>) obj).forEach(System.out::println);
            return;
        }
        if (obj instanceof Map) {
            ((Map<Object, Object>) obj).forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });
            return;
        }
        System.out.println(obj);
    }

    /**
     * 字符串包含中文
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean containChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 校验String是否全是中文
     *
     * @param name 被校验的字符串
     * @return true 代表全是汉字
     */
    public static boolean isAllChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!containChinese(String.valueOf(cTemp[i]))) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String templateStr = "{姓名}今年{岁}啦！";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("姓名", "王二小");
        paramMap.put("岁", "3");
        System.out.println(formatTemplate(templateStr, paramMap));
    }


    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor
     * of length 0.
     * <p>Note: this method returns {@code true} for a {@code CharSequence}
     * that purely consists of whitespace.
     * <p><pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     * @see #hasLength(String)
     * @see #hasText(CharSequence)
     */
    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>Note: this method returns {@code true} for a {@code String} that
     * purely consists of whitespace.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }


    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than
     * 0, and it contains at least one non-whitespace character.
     * <p><pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},
     * its length is greater than 0, and it does not contain whitespace only
     * @see #hasText(String)
     * @see #hasLength(CharSequence)
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     * @see #hasLength(String)
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String escapeSql(String text) {
        if (StringUtils.isNotBlank(text)) {
            return text.replace("'", "\\'")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    ;
        }
        return text;
    }
    /**
     * 中文数字
     */
    private final static String[] CHINESE_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    /**
     * 中文数字单位
     */
    private final static String[] CHINESE_NUMBER_UNIT = { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟" };
    /**
     * 人民币单位
     */
    private final static String[] CHINESE_MONEY_UNIT = { "圆", "角", "分" };

    /**
     * @param sourceMoney 要转换的数值，最多支持到亿
     * @return 结果
     */
    public static String toChineseMoney(BigDecimal sourceMoney) {
        if (new BigDecimal("1000000000000").compareTo(sourceMoney) <= 0
                && BigDecimal.ZERO.compareTo(sourceMoney) >= 0) {
            throw new RuntimeException("支持转换的金额范围为0~1万亿");
        }
        StringBuilder sb = new StringBuilder();
        // 整数部分
        BigDecimal intPart = sourceMoney.setScale(0, RoundingMode.DOWN);
        // 小数部分
        BigDecimal decimalPart = sourceMoney.subtract(intPart).multiply(new BigDecimal(100)).setScale(0,
                RoundingMode.DOWN);

        // 处理整数部分圆
        if (intPart.compareTo(BigDecimal.ZERO) > 0) {
            String intPartNumberString = intPart.toPlainString();
            int length = intPartNumberString.length();
            // 统计末尾的零，末尾零不做处理
            int zeroCount = 0;
            for (int i = length - 1; i >= 0; i--) {
                int number = Integer.parseInt(String.valueOf(intPartNumberString.charAt(i)));
                if (number == 0) {
                    zeroCount++;
                } else {
                    break;
                }
            }
            for (int i = 0; i < length; i++) {
                // 如果转换到末尾0，则停止转换
                if (i + zeroCount == length) {
                    break;
                }
                int number = Integer.parseInt(String.valueOf(intPartNumberString.charAt(i)));
                // 获取中文数字
                String chineseNumber = CHINESE_NUMBER[number];
                // 获取中文数字单位
                String chineseNumberUnit = CHINESE_NUMBER_UNIT[length - i - 1];
                sb.append(chineseNumber).append(chineseNumberUnit);
            }
            // 统计完后加上金额单位
            sb.append(CHINESE_MONEY_UNIT[0]);
        } else {
            sb.append(CHINESE_NUMBER[0]).append(CHINESE_MONEY_UNIT[0]);
        }

        // 处理小数部分
        if (decimalPart.compareTo(new BigDecimal(10)) >= 0) {
            // 角
            String jiao = decimalPart.toPlainString();
            int number = Integer.parseInt(String.valueOf(jiao.charAt(0)));
            if (number != 0) {
                String chineseNumber = CHINESE_NUMBER[number];
                sb.append(chineseNumber).append(CHINESE_MONEY_UNIT[1]);
            }

            // 分
            String fen = decimalPart.toPlainString();
            number = Integer.parseInt(String.valueOf(fen.charAt(1)));
            if (number != 0) {
                String chineseNumber = CHINESE_NUMBER[number];
                sb.append(chineseNumber).append(CHINESE_MONEY_UNIT[2]);
            }
        } else if (decimalPart.compareTo(BigDecimal.ZERO) > 0) {
            // 分
            String fen = decimalPart.toPlainString();
            int number = Integer.parseInt(String.valueOf(fen.charAt(0)));
            if (number != 0) {
                String chineseNumber = CHINESE_NUMBER[number];
                sb.append(chineseNumber).append(CHINESE_MONEY_UNIT[2]);
            }
        } else {
            sb.append("整");
        }
        return sb.toString();
    }

    public static String concat(Object... args) {
        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(Optional.ofNullable(arg).orElse(""));
            }
            return sb.toString();
        }
        return "";
    }
}
