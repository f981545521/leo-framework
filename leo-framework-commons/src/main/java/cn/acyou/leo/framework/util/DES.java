package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * DES加密介绍
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现
 * 。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
@Slf4j
public class DES {
    /**
     * 加密密钥 长度要是8的倍数
     */
    private static final String ENCRYPT_SECRET = "LEO-YYDS";

    //测试
    public static void main(String args[]) throws Exception {
        //待加密内容
        String str = UUID.randomUUID().toString();
        System.out.println("源字符串：" + str);
        String s = encryptWithBase64(str);
        System.out.println("加密后的Base64字符串：" + s);
        //9zivf8xQFZvw75jRBRnqWA==
        String s1 = decryptWithBase64(s);
        System.out.println("加密后的源串：" + s1);

    }

    /**
     * 加密并输出Base64字符串
     * @param str Base64字符串
     * @return 源字符串
     */
    public static String decryptWithBase64(String str){
        try {
            byte[] decode = Base64.getDecoder().decode(str);
            byte[] decrypt = DES.decrypt(decode, ENCRYPT_SECRET);
            return new String(decrypt, StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加密并输出Base64字符串
     * @param sourceStr 源字符串
     * @return Base64字符串
     */
    public static String encryptWithBase64(String sourceStr){
        try {
            byte[] result = DES.encrypt(sourceStr.getBytes(), ENCRYPT_SECRET);
            byte[] encode = Base64.getEncoder().encode(result);
            return new String(encode, StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    private static byte[] encrypt(byte[] datasource, String password) throws Exception{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        return cipher.doFinal(datasource);
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     */
    private static byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        return cipher.doFinal(src);
    }
}
