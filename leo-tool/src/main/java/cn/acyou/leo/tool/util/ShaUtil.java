package cn.acyou.leo.tool.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author youfang
 * @version [1.0.0, 2023/5/25 10:34]
 **/
@Slf4j
public class ShaUtil {

    /**
     * @param token     url相关的token
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return java.lang.String
     * @Description 用SHA1算法验证Token
     * @author lst
     * @date 2020-8-20 11:30
     */
    public static String getSHA1(String token, String timestamp, String nonce) {

        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.info("错误信息:{}", e.getMessage());
        }

        return tmpStr;
    }

    /**
     * @param byteArray
     * @return java.lang.String
     * @Description 将字节数组转换为十六进制字符串
     * @author lst
     * @date 2020-8-18 11:56
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            strDigest.append(byteToHexStr(byteArray[i]));
        }
        return strDigest.toString();
    }

    /**
     * @param mByte
     * @return java.lang.String
     * @Description 将字节转换为十六进制字符串
     * @author lst
     * @date 2020-8-18 11:57
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

}
