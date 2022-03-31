package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2018-05-23 上午 09:56]
 **/
@Data
public class WxConfig implements Serializable {

    private static final long serialVersionUID = -1803860272436488738L;

    /**
     * 公众号的唯一标识
     */
    private String appId;

    /**
     * 生成签名的时间戳
     */
    private String timestamp;

    /**
     * 生成签名的随机串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String signature;

    /**
     * 需要使用的JS接口列表
     */
    private List<String> jsApiList;
}
