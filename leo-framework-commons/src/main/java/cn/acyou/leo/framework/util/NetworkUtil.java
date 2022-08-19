package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author youfang
 * @version [1.0.0, 2020/12/29]
 **/
@Slf4j
public class NetworkUtil {

    /**
     * IP 可达
     *
     * @param ip 知识产权
     * @return boolean
     */
    public static boolean isReachable(String ip){
        int timeOut = 1000;
        try {
            return InetAddress.getByName(ip).isReachable(timeOut);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    /**
     * IP 不可达
     *
     * @param ip 知识产权
     * @return boolean
     */
    public static boolean notReachable(String ip){
        return !isReachable(ip);
    }

    public static void main(String[] args) {
        System.out.println(isReachable("192.168.1.120"));
        System.out.println(isReachable("192.168.12.120"));
    }

}
