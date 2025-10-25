package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * URL 处理工具
 * <pre>
 * String url = "https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/lifeLike/movie/movie001.mov?type=oss";
 * System.out.println(UrlUtil.getContentLength(new URL(url)));//16009340
 * System.out.println(UrlUtil.getName(url));//: movie001.mov
 * System.out.println(UrlUtil.getBaseName(url));//: movie001
 * System.out.println(UrlUtil.getExtension(url));//: mov
 * System.out.println(UrlUtil.getSearch(url));//: type=oss
 * System.out.println(UrlUtil.getFile(url));//: /lifeLike/movie/movie001.mov?type=oss
 * System.out.println(UrlUtil.getPathName(url));//: /lifeLike/movie/movie001.mov
 * System.out.println(UrlUtil.getQueryString(url));//: map:{type:oss}
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020-4-6 下午 06:20]
 **/
@Slf4j
public class UrlUtil {

    /**
     * 是URL
     *
     * @param url url
     * @return boolean 是/否
     */
    public static boolean isUrl(String url) {
        try {
            new URL(url);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 以 `http | https` 开头
     *
     * @param url url
     * @return boolean
     */
    public static boolean startWithHttp(String url) {
        if (url != null) {
            if (url.startsWith("http") || url.startsWith("https")) {
                return true;
            }
        }
        return false;
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
    public static String append(String url, Map<String, ?> params) {
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            url = append(url, entry.getKey(), entry.getValue().toString());
        }
        return url;
    }

    /**
     * 获取网址的Host
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> sale.vmall.com
     * </pre>
     *
     * @param url 网址
     * @return Host
     */
    public static String getHost(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getHost();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取网址的 Http://Host
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> https://sale.vmall.com
     * </pre>
     *
     * @param url 网址
     * @return Host
     */
    public static String getProtocolHost(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getProtocol() + "://" + url1.getHost();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取网址的path
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> /ttt/huaweizone.html
     * </pre>
     *
     * @param url 网址
     * @return pathname
     */
    public static String getPath(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getPath();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取网址最后的path
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> huaweizone.html
     * </pre>
     *
     * @param url 网址
     * @return pathname
     */
    public static String getLastPathName(String url) {
        String pathName = getPath(url);
        if (StringUtils.isNotBlank(pathName)) {
            String[] pathNameArray = pathName.split("/");
            return pathNameArray[pathNameArray.length - 1];
        }
        return "";
    }


    /**
     * 获取URL上可用的名称
     *
     * @param url 网址
     * @return {@link String}
     */
    public static String getUsableName(String url) {
        String name = UrlUtil.getName(url);
        if (StringUtils.isBlank(name)) {
            name = UrlUtil.getLastPathName(url);
        }
        return name;
    }

    /**
     * 获取网址的扩展名
     * <pre>
     *  https://sale.vmall.com/ttt/huaweizone.html?cid=10618
     *   -> html
     * </pre>
     *
     * @param url 网址
     * @return pathname
     */
    public static String getExtension(String url) {
        try {
            URL realUrl = new URL(url);
            String path = realUrl.getPath();
            if (path.contains(".")) {
                return path.substring(path.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
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
     * 得到URL上的文件名
     * <pre>
     * String url = "http://qiniu.acyou.cn/images/1.jpg?name=oss";
     * System.out.println(UrlUtil.getName(url));//==> 1
     * </pre>
     *
     * @param url url
     * @return {@link String}
     */
    public static String getBaseName(String url) {
        String name = getName(url);
        if (name.contains(".")) {
            return name.substring(0, name.indexOf("."));
        }
        return name;
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

    /**
     * 把header中的set-cookie转换为请求的字符串结构
     * <pre>
     *  Response Header示例:
     *      Connection: keep-alive
     *      Content-Encoding: gzip
     *      Content-Type: text/html; charset=utf-8
     *      Date: Sat, 12 Feb 2022 03:29:35 GMT
     *      Set-Cookie: kpf=PC_WEB; path=/; expires=Sun, 12 Feb 2023 03:29:35 GMT; domain=www.kuaishou.com; httponly
     *      Set-Cookie: kpn=KUAISHOU_VISION; path=/; expires=Sun, 12 Feb 2023 03:29:35 GMT; domain=www.kuaishou.com; httponly
     *      Set-Cookie: clientid=3; path=/; expires=Sun, 12 Feb 2023 03:29:35 GMT; domain=kuaishou.com; httponly
     *      Set-Cookie: did=web_c103091cbdefb56d3c25a450cdc3cea8; path=/; expires=Sun, 12 Feb 2023 03:29:35 GMT; domain=kuaishou.com
     *      Transfer-Encoding: chunked
     * </pre>
     *
     * @param setCookieHeaders header中的set-cookie
     * @return str
     */
    public static String getCookieStrFromSetCookie(List<String> setCookieHeaders) {
        if (CollectionUtils.isEmpty(setCookieHeaders)) {
            return "";
        }
        List<String> cookieList = new ArrayList<>();
        for (String setCookieHeader : setCookieHeaders) {
            String[] split = setCookieHeader.split(";");
            cookieList.add(split[0]);
        }
        return StringUtils.join(cookieList, "; ");
    }

    /**
     * 把CookieMap转换为请求的字符串结构
     * <pre>
     *     返回的：
     *     Cookie: did=web_46cefbd3406a4b29ad49fcaf48f01f1d; didv=1640161658000;
     * </pre>
     *
     * @param cookies cookies
     * @return str
     */
    public static String getCookieStr(Map<String, String> cookies){
        List<String> cookieList = new ArrayList<>();
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            cookieList.add(cookie.getKey() + "=" + cookie.getValue());
        }
        return StringUtils.join(cookieList, "; ");
    }

    /**
     * 把Cookie字符串转换为Map结构
     *
     * @param cookieStr cookie字符串，例如： did=web_46cefbd3406a4b29ad49fcaf48f01f1d; didv=1640161658000;
     * @return str
     */
    public static Map<String, String> getCookieMapFromCookieStr(String cookieStr){
        Map<String, String> cookieMap = new HashMap<>();
        if (cookieStr != null) {
            String[] cookieElements = cookieStr.split(";");
            for (String cookieEle : cookieElements) {
                String[] split = cookieEle.split("=");
                cookieMap.put(split[0], split[1]);
            }
        }
        return cookieMap;
    }

    /**
     * 编码
     *
     * @param url url
     * @return {@link String}
     */
    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return url;
    }

    /**
     * 解码
     *
     * @param url url
     * @return {@link String}
     */
    public static String decode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return url;
    }

    /**
     * 判断url是否可以访问
     *
     * @param url URL
     * @return ture or false
     */
    public static boolean canConnect(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        HttpURLConnection conn = null;
        try {
            URL url2 = new URL(url);
            conn = (HttpURLConnection) url2.openConnection();
            int state = conn.getResponseCode();
            if (state == 200) {
                return true;
            }
        } catch (Exception ex) {
            //ignore
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    /**
     * 移除URL上的?以及后面的参数
     * @param url https://.....
     * @return {@link String}
     */
    public static String removeQuery(String url){
        if (StringUtils.isNotBlank(url) && url.contains("?")) {
            return url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    public static String mapToQueryParamsStream(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        return params.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> {
                    try {
                        return URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()) +
                                "=" +
                                URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name());
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining("&"));
    }


    public static Map<String, String> convertToSingleValueMap(Map<String, String[]> sourceMap) {
        if (sourceMap == null) {
            return new HashMap<>();
        }
        return sourceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    String[] values = entry.getValue();
                    return (values != null && values.length > 0) ? values[0] : null;
                }
        ));
    }

    public static void main(String[] args) {

    }

    //public static void main(String[] args) throws Exception{
    //    String url = "https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/lifeLike/movie/movie001.mov?type=oss";
    //    System.out.println(UrlUtil.getContentLength(new URL(url)));//16009340
    //    System.out.println(UrlUtil.getName(url));//: movie001.mov
    //    System.out.println(UrlUtil.getBaseName(url));//: movie001
    //    System.out.println(UrlUtil.getExtension(url));//: mov
    //    System.out.println(UrlUtil.getSearch(url));//: type=oss
    //    System.out.println(UrlUtil.getFile(url));//: /lifeLike/movie/movie001.mov?type=oss
    //    System.out.println(UrlUtil.getPathName(url));//: /lifeLike/movie/movie001.mov
    //    System.out.println(UrlUtil.getQueryString(url));//: map:{type:oss}
    //}

}
