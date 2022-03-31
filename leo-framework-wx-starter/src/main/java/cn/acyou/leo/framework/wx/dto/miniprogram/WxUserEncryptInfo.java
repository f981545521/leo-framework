package cn.acyou.leo.framework.wx.dto.miniprogram;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/10]
 **/
@Data
public class WxUserEncryptInfo implements Serializable {
    private static final long serialVersionUID = 8700278727558199393L;

    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;
    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;

    class Watermark {
        private String appid;
        private long timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
