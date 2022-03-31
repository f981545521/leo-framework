package cn.acyou.leo.framework.wx.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 9:21]
 */
@ConfigurationProperties(prefix = "wx")
public class WxConfigProperties {
    /**
     * 微信公众号配置
     */
    private Mp mp = new Mp();
    /**
     * 微信小程序配置
     */
    private MiniProgram miniProgram = new MiniProgram();
    /**
     * 企业微信配置
     */
    private QyWx qyWx = new QyWx();

    public Mp getMp() {
        return mp;
    }

    public QyWx getQyWx() {
        return qyWx;
    }

    public void setQyWx(QyWx qyWx) {
        this.qyWx = qyWx;
    }

    public WxConfigProperties setMp(Mp mp) {
        this.mp = mp;
        return this;
    }

    public MiniProgram getMiniProgram() {
        return miniProgram;
    }

    public WxConfigProperties setMiniProgram(MiniProgram miniProgram) {
        this.miniProgram = miniProgram;
        return this;
    }

    @Data
    public static class Mp {
        /**
         * 公众号appid
         */
        private String appid;
        /**
         * 公众号appsecret
         */
        private String appsecret;
    }

    @Data
    public static class MiniProgram {
        /**
         * 小程序appid
         */
        private String appid;
        /**
         * 小程序appsecret
         */
        private String appsecret;
    }

    @Data
    public static class QyWx {
        /**
         * 企业ID
         */
        private String corpId;
        /**
         * 消息回调 Token
         */
        private String callbackToken;
        /**
         * 消息回调 EncodingAESKey
         */
        private String callbackEncodingAESKey;
        /**
         * 会话存档 secret
         *
         * <a href="https://developer.work.weixin.qq.com/document/path/91361">参考文档</a>
         */
        private String archiveSecret;
        /**
         * 会话存档 私钥版本
         * <p>
         * 获取的会话是配置的公钥加密后的，要使用此私钥解密消息
         *
         * <a href="https://developer.work.weixin.qq.com/document/path/91551">参考文档</a>
         */
        private Integer archivePrivateKeyVersion;
        /**
         * 会话存档 私钥
         * <p>
         * 获取的会话是配置的公钥加密后的，要使用此私钥解密消息
         *
         * <a href="https://developer.work.weixin.qq.com/document/path/91551">参考文档</a>
         */
        private String archivePrivateKey;
        /**
         * 企业微信 应用id （e.g. 1000247）
         */
        private String appAgentId;
        /**
         * 企业微信 应用的Secret
         */
        private String appSecret;

    }
}
