package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * SHA 加密方法
 *
 * @author youfang
 * @version [1.0.0, 2023/5/25 10:34]
 **/
@Slf4j
public class SHAUtil {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    /**
     * 用SHA1算法 加密 自然排序的数组
     *
     * @param strs 字符串数组
     * @return java.lang.String
     */
    public static String getSHA1(String... strs) {
        Arrays.sort(strs);
        StringBuilder content = new StringBuilder();
        for (String s : strs) {
            content.append(s);
        }
        return getSHA1(content.toString());
    }

    /**
     * 用SHA1算法验证Token
     *
     * @param token     url相关的token
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return java.lang.String
     */
    public static String getSHA1(String token, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        return getSHA1(content.toString());
    }


    /**
     * 使用 SHA1 加密字符串
     *
     * @param data 原字符串
     * @return The human-readable SHA1
     */
    public static String getSHA1(String data) {
        byte[] dataBytes = getDigest("SHA-1").digest(data.getBytes(UTF8_CHARSET));
        return new String(encodeHex(dataBytes));
    }
    /**
     * 使用 MD5 加密字符串
     *
     * @param data 原字符串
     * @return The human-readable MD5
     */
    public static String getMD5(String data) {
        byte[] dataBytes = getDigest("MD5").digest(data.getBytes(UTF8_CHARSET));
        return new String(encodeHex(dataBytes));
    }

    /**
     * 使用 SHA256 加密字符串
     *
     * @param data 原字符串
     * @return The human-readable SHA256
     */
    public static String getSHA256(String data) {
        byte[] dataBytes = getDigest("SHA-256").digest(data.getBytes(UTF8_CHARSET));
        return new String(encodeHex(dataBytes));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = HEX_CHARS[(0xF0 & data[i]) >>> 4];
            out[j++] = HEX_CHARS[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 使用给定的算法创建新的｛@link MessageDigest｝。必需，因为｛@code MessageDigest｝不是线程安全的。
     *
     * @param algorithm 算法
     * @return {@link MessageDigest}
     */
    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }

}
