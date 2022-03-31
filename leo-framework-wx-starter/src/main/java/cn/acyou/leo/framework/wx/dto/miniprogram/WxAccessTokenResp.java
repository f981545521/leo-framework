package cn.acyou.leo.framework.wx.dto.miniprogram;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/10]
 **/
@Data
public class WxAccessTokenResp implements Serializable {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private int expires_in;

    /**
     * 错误码
     */
    private int errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
