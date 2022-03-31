package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2018-05-23 上午 09:56]
 **/
@Data
public class QyWxAgentConfig implements Serializable {

    private static final long serialVersionUID = -1803860272436488738L;

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 应用ID
     */
    private String agentId;

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
