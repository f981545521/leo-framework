package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * jsapi_ticket是公众号用于调用微信JS接口的临时票据
 *
 * @author youfang
 * @version [1.0.0, 2018-05-23 上午 09:56]
 **/
@Data
public class JsapiTicket implements Serializable {
    private static final long serialVersionUID = -1803860272436488738L;

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误消息
     */
    private String errmsg;

    /**
     * jsapi_ticket是公众号用于调用微信JS接口的临时票据
     */
    private String ticket;

    /**
     * 有效期7200秒
     */
    private String expires_in;
}
