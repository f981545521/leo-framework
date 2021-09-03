package cn.acyou.leo.framework.util;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5算法
 *
 * @author fangyou
 * @since 2021/08/17
 */
public class Md5Util {

    /**
     * 校验MD5码
     *
     * @param sourceStr 要校验的字符串
     * @param md5Str  md5值
     * @return 校验结果
     */
    public static boolean valid(String sourceStr, String md5Str) {
        return md5Str.equalsIgnoreCase(md5(sourceStr));
    }

    /**
     * 得到对象的md5值
     * <p>原理：转成JSON字符串后再计算MD5值</p>
     *
     * @param obj obj
     * @return md5值
     */
    public static String getObjectMd5(Object... obj) {
        return md5(JSON.toJSONString(obj));
    }

    /**
     * 计算加盐md5
     *
     * @param sourceStr 源str
     * @param salt      盐
     * @return {@link String}
     */
    public static String md5(String sourceStr, String salt) {
        return md5(sourceStr + salt);
    }


    /**
     * 计算字符串的md5值（32位 数字+大写字母）
     *
     * @param sourceStr 源str
     * @return {@link String}
     */
    public static String md5Upper(String sourceStr) {
        return md5(sourceStr).toUpperCase();
    }

    /**
     * 计算字符串的md5值（32位 数字+小写字母）
     *
     * @param sourceStr 源str
     * @return {@link String}
     */
    public static String md5(String sourceStr) {
        return md5(sourceStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算byte数组的md5值（32位 数字+小写字母）
     *
     * @param bytes byte数组
     * @return {@link String}
     */
    public static String md5(byte[] bytes) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            byte[] encryption = md5.digest();
            StringBuilder strBuf = new StringBuilder();
            for (byte b : encryption) {
                if (Integer.toHexString(0xff & b).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & b));
                } else {
                    strBuf.append(Integer.toHexString(0xff & b));
                }
            }
            return strBuf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(md5("admin"));
        System.out.println(md5Upper("admin"));
        System.out.println(md5("admin", "admin123"));
    }
}
