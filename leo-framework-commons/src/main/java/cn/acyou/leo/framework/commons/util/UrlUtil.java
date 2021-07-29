package cn.acyou.leo.framework.commons.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-6 下午 06:20]
 **/
public class UrlUtil {

    public static final String SLASH = "\\";

    /**
     * 从URL中提取参数Map
     *
     * @param url 路径
     * @return 参数Map
     */
    public static String getQueryString(String url, String key) {
        Map<String, String> paramsMap = getQueryString(url);
        return paramsMap.get(key) != null ? paramsMap.get(key) : "";
    }

    /**
     * 获取JSONP JSON
     *
     * @param jsonpStr jsonpStr
     * @return JSON
     */
    public static String getJsonPJsonStr(String jsonpStr) {
        int startIndex = jsonpStr.indexOf("(");
        int endIndex = jsonpStr.lastIndexOf(")");
        return jsonpStr.substring(startIndex + 1, endIndex);
    }


    /**
     * 获取指定节点的 Str
     *
     * @param jsonStr json Str
     * @param key     关键
     * @return {@link String}
     */
    public static String getAppointJsonStr(String jsonStr, String key) {
        JSONObject obj = JSONObject.parseObject(jsonStr);
        Object o = obj.get(key);
        if (o != null) {
            return JSON.toJSONString(o);
        } else {
            return "";
        }
    }

    /**
     * 从URL中提取参数Map
     *
     * @param url 路径
     * @return 参数Map
     */
    public static Map<String, String> getQueryString(String url) {
        Map<String, String> map = new HashMap<>();
        int start = url.indexOf("?");
        if (start >= 0) {
            String str = url.substring(start + 1);
            String[] paramsArr = str.split("&");
            for (String param : paramsArr) {
                String[] temp = param.split("=");
                map.put(temp[0], temp[1]);
            }
        }
        return map;
    }

    /**
     * 从URL中提取Path参数
     *
     * @param url 路径
     * @return 参数Map
     */
    public static String getQueryStringFromPath(String url) {
        int start = url.lastIndexOf("/") + 1;
        if (url.lastIndexOf("?") == -1) {
            return url.substring(start);
        }
        return url.substring(start, url.lastIndexOf("?"));
    }


    /**
     * 替换URL中的参数 URL 参数替换
     * <pre>
     *         String url = "https://sou.zhaopin.com/?jl=635&kw=编导&kt=3";
     *         Map<String, String> replaceMap = new HashMap<>();
     *         replaceMap.put("kt", "8");
     *         replaceMap.put("jl", "ojbk");
     *         System.out.println(replaceUrlParams(url, replaceMap));// -> https://sou.zhaopin.com/?jl=ojbk&kw=编导&kt=8
     * </pre>
     *
     * @param url 路径
     * @return newUrl
     */
    public static String replaceUrlParams(String url, Map<String, String> newParams) {
        String newUrl = url;
        for (Map.Entry<String, String> entry : newParams.entrySet()) {
            Pattern pattern = Pattern.compile("([?&])" + entry.getKey() + "=([^&]+)");
            Matcher matcher = pattern.matcher(newUrl);
            if (matcher.find()) {
                String group = matcher.group(0);
                if (group.startsWith("?")) {
                    newUrl = matcher.replaceAll("?" + entry.getKey() + "=" + entry.getValue());
                }
                if (group.startsWith("&")) {
                    newUrl = matcher.replaceAll("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return newUrl;
    }

    /**
     * 替换URL中的参数     URL 参数替换
     * 注意：参数必须为偶数 第一个为key,第二个为value
     *
     * <pre>
     *         String url = "https://sou.zhaopin.com/?jl=635&kw=编导&kt=3";
     *         System.out.println(replaceUrlParams(url, "kt","GGG"));//  -> https://sou.zhaopin.com/?jl=635&kw=编导&kt=GGG
     * </pre>
     *
     * @param url 路径
     * @return newUrl
     */
    public static String replaceUrlParams(String url, String... params) {
        Map<String, String> newParamsMap = new HashMap<>();
        int length = params.length;
        if (length == 0) {
            return url;
        }
        if (length % 2 != 0) {
            throw new IllegalArgumentException("参数错误，不能为奇数");
        }
        for (int i = 0; i < params.length; i = i + 2) {
            newParamsMap.put(params[i], params[i + 1]);
        }
        return replaceUrlParams(url, newParamsMap);
    }


    /**
     * 为URL追加参数
     * <pre>
     *     append("https://sou.zhaopin.com/access_token=22222&", "kt", "GGG", "sign", "1111111111")
     *     -> https://sou.zhaopin.com/access_token=22222&kt=GGG&sign=1111111111
     * </pre>
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    public static String append(String url, String... params) {
        List<String> paramList = new ArrayList<>();
        int length = params.length;
        if (length == 0) {
            return url;
        }
        if (length % 2 != 0) {
            throw new IllegalArgumentException("参数错误，不能为奇数");
        }
        for (int i = 0; i < params.length; i = i + 2) {
            paramList.add("&" + params[i] + "=" + params[i + 1]);
        }
        if (url.endsWith("&")){
            url = url.substring(0, url.length() - 1);
        }
        return url + StringUtils.join(paramList, "");
    }

    public static void main(String[] args) {
        String url = "https://sou.zhaopin.com/?jl=635&kw=编导&kt=3";
        System.out.println(replaceUrlParams(url, "kt", "GGG"));

    }

}
