package cn.acyou.leo.framework.util;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验工具类
 *
 * @author youfang
 * @version [1.0.0, 2020年07月23日]
 */
public class RegexUtil extends RegExUtils {
    /**
     * IP地址正则表达式
     */
    private static final Pattern REGEX_IP_ADDRESS = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
    /**
     * 邮箱正则表达式
     */
    private static final Pattern REGEX_E_MAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    /**
     * 中文（汉字）正则表达式
     */
    private static final Pattern REGEX_CHINESE = Pattern.compile("[\u4e00-\u9fa5]");
    /**
     * 数字正则表达式
     */
    private static final Pattern REGEX_NUMBER = Pattern.compile("^-?\\d+$");
    /**
     * 日期字符串正则表达式
     */
    private static final Pattern REGEX_DATE_STR = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");

    /**
     * 日期+时间字符串正则表达式
     */
    private static final Pattern REGEX_DATE_TIME_STR = Pattern.compile("((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) (20|21|22|23|[0-1]{1}[0-9]):[0-5]{1}[0-9]:[0-5]{1}[0-9]");
    /**
     * 时间字符串正则表达式
     */
    private static final Pattern REGEX_TIME_STR = Pattern.compile("(0[1-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
    /**
     * 密码强度正则
     */
    public static final String REGEX_STRONG_PASSWORD = "^.{6,16}$";
    /**
     * 固定电话号码("XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX)
     */
    public static final String REGEX_TELEPHONE = "\\d{3}-\\d{8}|\\d{4}-\\{7,8}";
    /**
     * 移动电话
     */
    public static final String REGEX_MOBILEPHONE = "^1[1-9][0-9]\\d{8}$";
    /**
     * 数字字母组合
     */
    public static final String REGEX_NUMBER_LETTER_COMBINATION = "^[A-Za-z0-9]+$";
    /**
     * 数字字母符号组合
     */
    public static final String REGEX_NUMBER_LETTER_SYMBOL_COMBINATION = "^[A-Za-z0-9,./<>?;':\"\\[\\]{}\\-=!@#$%^&*()_+]+$";
    /**
     * 非数字
     */
    public static final Pattern NOT_NUMBER = Pattern.compile("[^0-9]");

    /**
     * 判断是否是正确的IP地址
     *
     * @param ip ip地址
     * @return boolean true,通过，false，没通过
     */
    public static boolean isIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        Matcher matcher = REGEX_IP_ADDRESS.matcher(ip);
        return matcher.find();
    }

    /**
     * 判断是否是正确的邮箱地址
     *
     * @param email 邮箱
     * @return boolean true,通过，false，没通过
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher matcher = REGEX_E_MAIL.matcher(email);
        return matcher.find();
    }

    /**
     * 判断是否**含有**中文，仅适合中国汉字，不包括标点
     *
     * @param text 文字
     * @return boolean true,通过，false，没通过
     */
    public static boolean isChinese(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        Matcher m = REGEX_CHINESE.matcher(text);
        return m.find();
    }

    /**
     * 提取所有文本中正则匹配的字符
     *
     * @param text  文本
     * @param regex 正则表达式
     * @return 文本中正则匹配的字符
     */
    public static String extractAll(String text, String regex){
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        Matcher matcher = Pattern.compile(regex).matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }


    /**
     * 判断是否正整数
     *
     * @param number 数字
     * @return boolean true,通过，false，没通过
     */
    public static boolean isNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        Matcher m = REGEX_NUMBER.matcher(number);
        return m.find();
    }

    /**
     * 判断几位小数(正数)
     *
     * @param decimal 数字
     * @param count   小数位数
     * @return boolean true,通过，false，没通过
     */
    public static boolean isDecimal(String decimal, int count) {
        if (StringUtils.isEmpty(decimal)) {
            return false;
        }
        String regex = "^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){" + count + "})?$";
        return decimal.matches(regex);
    }

    /**
     * 判断是否是手机号码
     *
     * @param phoneNumber 手机号码
     * @return boolean true,通过，false，没通过
     */
    public static boolean isMobilePhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return phoneNumber.matches(REGEX_MOBILEPHONE);
    }
    /**
     * 判断是否是固话号码
     *
     * @param phoneNumber 固话号码
     * @return boolean true,通过，false，没通过
     */
    public static boolean isTelephone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return phoneNumber.matches(REGEX_TELEPHONE);
    }

    /**
     * 电话号码或手机号码
     *
     * @param phoneNumber 号码
     * @return boolean
     */
    public static boolean isTelephoneOrMobilephone(String phoneNumber){
        return isMobilePhone(phoneNumber) || isTelephone(phoneNumber);
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param text 文本
     * @return boolean true,通过，false，没通过
     */
    public static boolean hasSpecialChar(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        // 如果不包含特殊字符
        return text.replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0;
    }

    /**
     * 是chinese2
     * 适应CJK（中日韩）字符集，部分中日韩的字是一样的
     *
     * @param strName str的名字
     * @return boolean
     */
    public static boolean isChinese2(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是日期字符串
     *
     * @param dateStr str日期
     * @return boolean
     */
    public static boolean isDateStr(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return false;
        }
        Matcher m = REGEX_DATE_STR.matcher(dateStr);
        return m.find();
    }

    /**
     * 是字母和数字组合
     *
     * @param str 固话号码
     * @return boolean true,通过，false，没通过
     */
    public static boolean isNumberLetter(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.matches(REGEX_NUMBER_LETTER_COMBINATION);
    }
    /**
     * 是字母和数字和符号组合
     *
     * @param str 固话号码
     * @return boolean true,通过，false，没通过
     */
    public static boolean isNumberLetterSymbol(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.matches(REGEX_NUMBER_LETTER_SYMBOL_COMBINATION);
    }

    /**
     * 是日期时间字符串
     *
     * @param dateTimeStr 日期时间
     * @return boolean
     */
    public static boolean isDateTime(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return false;
        }
        Matcher m = REGEX_DATE_TIME_STR.matcher(dateTimeStr);
        return m.find();
    }
    /**
     * 是时间字符串
     *
     * @param timeStr 时间 format with "hh:mm:ss"
     * @return boolean
     */
    public static boolean isTime(String timeStr){
        if (StringUtils.isEmpty(timeStr)) {
            return false;
        }
        Matcher m = REGEX_TIME_STR.matcher(timeStr);
        return m.find();
    }

    /**
     * 是中文字符
     *
     * @param c char
     * @return boolean
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public static int onlyKeepNumbers(String str){
        String s = NOT_NUMBER.matcher(str).replaceAll("");
        return Integer.parseInt(s);
    }

    /**
     * 密码强度校验
     *
     * @param password 密码
     */
    public static boolean isStrongPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return password.matches(REGEX_STRONG_PASSWORD);
    }

    /**
     * 得到匹配str
     *
     * @param sourceStr 源str
     * @param regex     正则表达式
     * @return {@link List <String>}
     */
    public static List<String> getMatchStr(String sourceStr, String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(sourceStr);
        List<String> res = new ArrayList<>();
        while (matcher.find()){
            res.add(matcher.group());
        }
        return res;
    }

    public static void main(String[] args) {
        String s = "the request was rejected because its size (57176420) exceeds the configured maximum (52428800)";
        List<String> matchStr = RegexUtil.getMatchStr(s, "\\d+");
        System.out.println(matchStr);
    }
}
