package cn.acyou.leo.framework.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * -----BEGIN PUBLIC KEY-----
 * ...
 * -----END PUBLIC KEY-----
 *
 * -----BEGIN PRIVATE KEY-----
 * ...
 * -----END PRIVATE KEY-----
 *
 *
 * @author youfang
 * @version [1.0.0, 2022/1/13 15:55]
 **/
public class RSAUtils {
    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 2048;
    /**
     * 用于存放随机产生的公钥与私钥
     */
    private static final Map<Integer, String> keyMap = new HashMap<Integer, String>();

    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥和私钥保存到Map
        //0表示公钥
        keyMap.put(0, publicKeyString);
        //1表示私钥
        keyMap.put(1, privateKeyString);
        System.out.println("生成完毕。");
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    public static void printFormatPublicKey(String key){
        List<String> contentList = new ArrayList<>();
        contentList.add("-----BEGIN PUBLIC KEY-----");
        contentList.addAll(StringUtils.splitByLength(key, 64));
        contentList.add("-----END PUBLIC KEY-----");
        for (String s : contentList) {
            System.out.println(s);
        }
    }

    public static void printFormatPrivateKey(String key){
        List<String> contentList = new ArrayList<>();
        contentList.add("-----BEGIN PRIVATE KEY-----");
        contentList.addAll(StringUtils.splitByLength(key, 64));
        contentList.add("-----END PRIVATE KEY-----");
        for (String s : contentList) {
            System.out.println(s);
        }
    }

    public static void mai2n(String[] args) throws Exception {
        String data = "ODZRohQ7H8+a24N2kVu1205HF6SWZFMr23OWF9hGu9CHDYHusuawM5eXTqBVVUR2CmIq2dksPzsUmg6fylluHeL+Xa1snb3E0ZEq7lqv+8Eapm56DmWWijuL4Qx43PDjgdGLh22rJmlzwrse2rqvGjKwuAIrL7u8LwXhuIEbnoxfTyzU0hJtHZyhv+y7uoPfU+/RvCXRk9HzLTI1r+0Eqx12VwtaqoYbvLJc7dAegbMqKHcLZI0l/bVbzSWNwsrph86BfFB7P3POxYoe2LfX/waqFSPBfzUFvzzj0LGwxD/sEbGl22vUBzRoscfj2VDgVsSFnJr4c4g9QEWMxBj+Zw==";
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKj3oOuducNXpKQMtNtg2bsDaQ5EYEhdEvX9y22nvxurjpZKsY3c/ZPQz/aluJCMjTQIkiDAXE6S3DoNMBEXntwc5kCBwLkByvWvKMIUVU/LwYHgY6ZBn5pFA8hEiqrl54fAos5471LqGCgYzRKMMB+11WIeR3BFH02sRFGdLKLTKta3EXDPS1hY3X+yvnL0EqX+G9t9Pd4WPu1MKnHH9+6uHi9cq0uHz5+GQX+DIbNFW8YB5YIolYT86GZgrpiEfa+0WYLrbLfAN4ImPU14N0HgnQ4bi0iadsyC/sMjx++hAPb7ZYkWSqs+aSLaa4ihsFjX9+aRKSSCJ1Vo4JHgbBAgMBAAECggEAI/cX9IiTk1liM7Hb04EEyTuU/kjyT6w4XvagAKp3b4hLBPHHY333vGiXXhjKR6iLECJ5Negu+yyXX2b4Phe+MVOT8e0U8n9morGVsX/VUjkDzbLFZRSklWLsHLktb3gxN07jCULVnZiL/acI2SWecH5BfODxP19UspyyQ0lvqGBupTL5R0yOBTIjJMSzOPca8yvd50wQxHokXzFlGw14lp8sb7dB0JcIVCxMgDtqPjLheWdAihD4d38Jso1659O53qQk6sQ+M1cWmWTnzBMUGagykuZJEF8a0D5+Dgm4x1VTP+7dKALBbnDZ/vNkN8QW8gLo8m3sdVIwdFVwO8RyvQKBgQDR4W2O0UOugsfl3S2FQETu9Mj7laMfZ+jrb9HH1a4KCxoQuHC6cE6eQNCFLsBbxGwGVBLO2KtkOGyqQ2tWakUCI7pnxcy5VEL8QHQenqW+xhdblmjFioBwZZHduztPa4+WqMC/bSzi4C8FJTr31oLmAfSGbltc1fwcra784GWvewKBgQCpAgYQOnjJce35QuAxDGbDZ4ZrTvQAlgLunILoVMGUjrZFA/+s7jb1zjMxS/iy/pfL/pnw/B/WVefox10LQB9TGp9OYBFIR+8nNGCdO+4TKi0z6HEnrPXjnvKQyZiCyIe0sMxqDAFbYgOSfwBFGbw4yjPX+s5ikhMUiQG/ZtzP8wKBgQCtw1iEXIcPjWFY0ySj5NDtkaZwfLQABXSWsA5bx3J/CC/9VeamxOKiLQE2sr05fMb++k5UCJjlsb1tvcG5WuRlYBUtY1sauUN4OxioXSrWZoTiJAX8Pq4V4FHC5Qe2ulO4JWDnW8lE0Ny9AtTuaiAXs9iyD93+X3+KctWksVgezwKBgDwUzhcC0mG6sMDF93E4WWfp661GOhPjQFOXLCpvxwmK2l0amM/fN4aWsTg7K8RmIYyRjCRRXPgTPghQp1ESkJA4jJ2OJVauy+QCCL6cyXviqIMlhOR35knu/YhfQmpr42Jm36RrdCgKZf60S2JFTdZae6eyL6B3EXVhZ0+lv6XlAoGBAKU8wFSyBXxrAwQwmjzq1NOOBeqzQID04tTdtwW8rho8T1QNpivAgpzu3YbBXl2eImbdWqP3as8oaNRbxbT5B2Pby/qPo2iUqKJTfD+ruqjH2QAUw3QLQEOxLA8WFHuBxA56cqbBWtP6heYwZBK/bqyFMka2Fc6o3I8SCg2lmhJw";
        String s = decrypt(data, privateKey);
        System.out.println(s);//6QFc6Dc+qTo/ado8RyniFbU3ackxAhAInaUsh4xbQCM+CLHZ4j+ceWqBQRPgE0X4VgLpXbHPnd9TyB283EQqBA==
    }

    public static void main(String[] args) throws Exception {
        long temp = System.currentTimeMillis();
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        System.out.println("公钥:\r\n" + keyMap.get(0));
        printFormatPublicKey(keyMap.get(0));
        System.out.println("私钥:\r\n" + keyMap.get(1));
        printFormatPrivateKey(keyMap.get(1));
        System.out.println("生成密钥消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
        String message = "RSA测试---->";
        System.out.println("原文:" + message);
        temp = System.currentTimeMillis();
        String messageEn = encrypt(message, keyMap.get(0));
        System.out.println("密文:" + messageEn);
        System.out.println("加密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
        temp = System.currentTimeMillis();
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("解密:" + messageDe);
        System.out.println("解密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
    }
}