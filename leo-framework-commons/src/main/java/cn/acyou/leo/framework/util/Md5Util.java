package cn.acyou.leo.framework.util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5算法
 */
public class Md5Util {

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes(StandardCharsets.UTF_8));
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

    public static String string2Md5(String plainText) {
        StringBuilder buf = null;
        String bufInfo = "";
        try {
            if (null != plainText) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plainText.getBytes());
                byte[] b = md.digest();
                int i;
                buf = new StringBuilder("");
                for (byte value : b) {
                    i = value;
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        buf.append("0");
                    }
                    buf.append(Integer.toHexString(i));
                }
            }
            if (null != buf) {
                bufInfo = buf.toString().substring(8, 24);// 16位的加密
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bufInfo;
    }


    public static String string2Md5for32(String plainText) {
        StringBuilder buf = null;
        String bufInfo = "";
        try {
            if (null != plainText) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plainText.getBytes());
                byte[] b = md.digest();
                int i;
                buf = new StringBuilder("");
                for (byte value : b) {
                    i = value;
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        buf.append("0");
                    }
                    buf.append(Integer.toHexString(i));
                }
            }
            if (null != buf) {
                bufInfo = buf.toString().toUpperCase();// 32位的加密
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bufInfo;
    }


    public static void main(String[] args) {
        System.out.println(string2Md5for32("123456789"));
    }
}
