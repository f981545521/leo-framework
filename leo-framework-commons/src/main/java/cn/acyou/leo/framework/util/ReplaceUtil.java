package cn.acyou.leo.framework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youfang
 * @version [1.0.0, 2021-7-8]
 **/
public class ReplaceUtil {
    /**
     * {location}地区，{location}当前温度，{temp}摄氏度，{real-weather}
     * 匹配大括号
     */
    private static final Pattern REGEX_BRACE = Pattern.compile("\\{([^\\)]*?)\\}");

    /**
     * 替换text中的 key value
     *
     * @param text text
     * @return newUrl
     */
    public static String replaceParams(String text, Map<String, String> params) {
        Matcher matcher = REGEX_BRACE.matcher(text);
        String newText = text;
        while (matcher.find()) {
            String keyWord = matcher.group();
            String keyWordValue = params.get(keyWord);
            if (keyWordValue != null){
                newText = newText.replace(keyWord, keyWordValue);
            }
        }
        return newText;
    }

    public static void main(String[] args) {
        String text = "{location}地区，{location}当前温度，{temp}摄氏度，{real-weather}";
        Map<String, String> params = new HashMap<>();
        params.put("{location}", "南京");
        params.put("{temp}", "28°");
        params.put("{temp2}", "28°");
        params.put("{real-weather}", "25°");
        System.out.println(replaceParams(text, params));
    }

}
