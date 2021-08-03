package cn.acyou.leo.framework.util;

import org.apache.commons.collections4.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP地址工具
 *
 * @author youfang
 * @version [1.0.0, 2020/7/10]
 **/
public class IPUtil {
    /**
     * 检查IP是否合法
     *
     * @param ip ip地址
     * @return true/false
     */
    public static boolean ipValid(String ip) {
        String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
        String regex1 = "1\\d{2}";
        String regex2 = "[1-9]\\d";
        String regex3 = "\\d";
        String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
        regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }


    /**
     * 获取本地ip 适合windows与linux
     *
     * @return {@link String}
     */
    public static String getLocalIP() {
        String defaultLocalIp = "127.0.0.1";
        List<String> localIps = getLocalIPList();
        if (CollectionUtils.isNotEmpty(localIps)){
            return localIps.get(0);
        }
        return defaultLocalIp;
    }

    /**
     * 获取本地所有网卡ip 适合windows与linux
     *
     * @return {@link String}
     */
    public static List<String> getLocalIPList() {
        List<String> localIps = new ArrayList<>();
        try {
            //获取本地所有网络接口
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            //遍历枚举中的每一个元素
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enumInetAddr = ni.getInetAddresses();
                while (enumInetAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        localIps.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localIps;
    }


    /**
     * 获取客户机的ip地址
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(",") + 1).trim();
        }
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(getLocalIP());
    }

}
