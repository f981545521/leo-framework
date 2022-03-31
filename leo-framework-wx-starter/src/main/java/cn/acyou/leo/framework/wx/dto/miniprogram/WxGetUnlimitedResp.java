package cn.acyou.leo.framework.wx.dto.miniprogram;

import lombok.Data;

/**
 * 微信消息返回信息
 *
 * @author youfang
 * @version [1.0.0, 2020/9/4]
 **/
@Data
public class WxGetUnlimitedResp {
    /**
     * 错误码 0 -ok
     */
    private Integer errcode;
    /**
     * 错误信息
     */
    private String errmsg;
    /**
     * 数据类型 (MIME Type)
     */
    private String contentType;
    /**
     * 数据 Buffer 二进制数据
     */
    private String buffer;
}
