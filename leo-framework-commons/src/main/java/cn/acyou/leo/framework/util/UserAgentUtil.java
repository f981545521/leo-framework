package cn.acyou.leo.framework.util;

import com.google.common.collect.Lists;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class UserAgentUtil {
    /**
     * iPhone 浏览器
     */
    public static final String USER_AGENT_APPLE = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    /**
     * PC 浏览器
     */
    public static final String PC_BROWSER = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
    /**
     * Android 浏览器
     */
    private static final String USER_AGENT_ANDROID = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Mobile Safari/537.36";

    /**
     * 是否微信浏览器
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isWeChat(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent").toLowerCase();
        //Windows的微信浏览器：Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1301.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2875.116 Safari/537.36 NetType/WIFI MicroMessenger/7.0.5 WindowsWechat
        if (ua.contains("micromessenger") && !ua.contains("windowswechat")) {
            //微信
            return true;
        }
        //非微信手机浏览器
        return false;
    }

    /**
     * 判断 是安卓端
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isAndroid(HttpServletRequest request) {
        List<String> mobileAgents = Lists.newArrayList("android");
        String ua = request.getHeader("User-Agent").toLowerCase();
        for (String sua : mobileAgents) {
            if (ua.contains(sua)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断 移动端/PC端
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isMobile(HttpServletRequest request) {
        List<String> mobileAgents = Arrays.asList("ipad", "iphone os", "rv:1.2.3.4", "ucweb", "android", "windows ce", "windows mobile");
        String ua = request.getHeader("User-Agent").toLowerCase();
        for (String sua : mobileAgents) {
            if (ua.contains(sua)) {
                //手机端
                return true;
            }
        }
        //PC端
        return false;
    }

    /**
     * 不是移动端
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isPcWeb(HttpServletRequest request) {
        return !isMobile(request);
    }


}
