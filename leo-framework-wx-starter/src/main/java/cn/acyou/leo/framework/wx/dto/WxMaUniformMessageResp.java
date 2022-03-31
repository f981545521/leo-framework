package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

/**
 * 微信消息返回信息
 *
 * @author youfang
 * @version [1.0.0, 2020/9/4]
 **/
@Data
public class WxMaUniformMessageResp {
    /**
     * 错误码 0 -ok
     */
    private Integer errcode;
    /**
     * 错误信息
     */
    private String errmsg;
}
