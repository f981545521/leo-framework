package cn.acyou.leo.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
     * 是URL
     *
     * @param url url
     * @return boolean 是/否
     */
    public static boolean isUrl(String url){
        try {
            new URL(url);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

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
                if (temp.length == 1){
                    map.put(temp[0], "");
                }else {
                    map.put(temp[0], temp[1]);
                }
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
        if (!url.contains("?")){
            url = url.concat("?");
        }
        return url + StringUtils.join(paramList, "");
    }

    /**
     * 为URL追加参数
     *
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    public static String append(String url, Map<String, ?> params){
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            url = append(url, entry.getKey(), entry.getValue().toString());
        }
        return url;
    }

    /**
     * 获取网址的path
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> /ttt/huaweizone.html
     * </pre>
     * @param url 网址
     * @return pathname
     */
    public static String getPathName(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getPath();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取指定URL对应资源的内容长度，对于Http，其长度使用Content-Length头决定。
     *
     * @param url URL
     * @return 内容长度，未知返回-1
     */
    public static long getContentLength(URL url) {
        if (null == url) {
            return -1;
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            return conn.getContentLengthLong();
        } catch (IOException e) {
            return -1;
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }


    /**
     * 得到URL上的文件名
     * <pre>
     * String url = "http://qiniu.acyou.cn/images/1.jpg?name=oss";
     * System.out.println(UrlUtil.getName(url));//==> 1.jpg
     * </pre>
     *
     * @param url url
     * @return {@link String}
     */
    public static String getName(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getPath().substring(url1.getPath().lastIndexOf("/") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取网址的path
     * <pre>
     *  https://sale.vmall.com/huaweizone.html?cid=10618
     *   -> cid=10618
     * </pre>
     * @param url 网址
     * @return pathname
     */
    public static String getSearch(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getQuery();
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 获取网址的path
     * <pre>
     *  http://qiniu.acyou.cn/images/1.jpg?name=oss
     *   -> /images/1.jpg?name=oss
     * </pre>
     * @param url 网址
     * @return pathname
     */
    public static String getFile(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getFile();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) throws Exception{
        String url = "https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/lifeLike/movie/movie001.mov";
        System.out.println("start");
        System.out.println(UrlUtil.getContentLength(new URL(url)));
        System.out.println("end");
        System.out.println(UrlUtil.getName(url));
        System.out.println(UrlUtil.getSearch(url));
        System.out.println(UrlUtil.getPathName(url));
        System.out.println(UrlUtil.getPathName(url));

    }

}
