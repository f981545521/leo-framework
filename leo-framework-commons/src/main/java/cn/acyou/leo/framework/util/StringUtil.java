package cn.acyou.leo.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 补充 String 相关方法
 *
 * @author youfang
 * @version [1.0.0, 2020-7-24 下午 09:31]
 **/
public class StringUtil {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    /**
     * 换行
     */
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
     * 数据库 like %xx%
     *
     * @param value 价值
     * @return {@link String}
     */
    public static String likeLR(String value) {
        return "%" + value + "%";
    }

    /**
     * 字符串加入逗号
     *
     * @param iterable 可迭代的
     * @return {@link String}
     */
    public static String joinOnComma(final Iterable<?> iterable) {
        return StringUtils.join(iterable.iterator(), COMMA);
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
    public static String nullAsDash(String str) {
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
    public static String isNullOrBlank(Object value, Object defaultValue) {
        if (isNullOrBlank(value)) {
            return defaultValue.toString();
        }
        return value.toString();
    }

    /**
     * 判断字符串是否是空或空串
     *
     * @param str 字符串
     * @return 是否是空或空串
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 判断字符串是否是空或空串
     *
     * @param str 字符串
     * @return 是否是空或空串
     */
    public static boolean isNotNullOrBlank(String str) {
        return !isNullOrBlank(str);
    }

    /**
     * 不是null或空字符串
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNotNullOrBlank(Object obj) {
        return obj != null && isNotNullOrBlank(obj.toString());
    }

    /**
     * 不是null或空字符串 返回默认值
     *
     * @param obj          obj
     * @param defaultValue 默认值
     * @return {@link String}
     */
    public static String isNotNullOrBlank(Object obj, Object defaultValue) {
        return isNotNullOrBlank(obj) ? obj.toString() : defaultValue.toString();
    }

    /**
     * 是null或空字符串
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNullOrBlank(Object obj) {
        return obj == null || isNullOrBlank(obj.toString());
    }

    /**
     * str长度分割
     *
     * @param sourceStr 源str
     * @param length    长度 (几个汉字)
     * @return str array
     */
    public static String[] strLengthSplit(String sourceStr, int length) {
        if (isNullOrBlank(sourceStr)) {
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

    public static void main(String[] args) {
        String templateStr = "{姓名}今年{岁}啦！";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("姓名", "王二小");
        paramMap.put("岁", "3");
        System.out.println(formatTemplate(templateStr, paramMap));
    }

}
