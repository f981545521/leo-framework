package cn.acyou.leo.framework.util;

import java.util.*;

/**
 * 补充 Spring String 相关方法
 *
 * @author youfang
 * @version [1.0.0, 2020-7-24 下午 09:31]
 **/
public class StringUtils extends org.springframework.util.StringUtils {
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
     * 判断字符串是否是空或空串
     *
     * @param str 字符串
     * @return 是否是空或空串
     */
    public static boolean isBlank(String str) {
        return !hasText(str);
    }

    /**
     * 判断字符串是否是空或空串
     *
     * @param str 字符串
     * @return 是否是空或空串
     */
    public static boolean isNotBlank(String str) {
        return hasText(str);
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
            ((Map<Object, Object>) obj).forEach((k,v)->{
                System.out.println(k + ":" + v);
            });
            return;
        }
        System.out.println(obj);
    }


    public static void main(String[] args) {
        String templateStr = "{姓名}今年{岁}啦！";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("姓名", "王二小");
        paramMap.put("岁", "3");
        System.out.println(formatTemplate(templateStr, paramMap));
    }

}
