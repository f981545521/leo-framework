package cn.acyou.leo.framework.wx.dto.miniprogram;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户小程序响应信息
 *
 * @author youfang
 * @date 2020/07/09
 */
@Data
public class WxCode2SessionResp implements Serializable {
    private static final long serialVersionUID = -1430730556893870658L;
    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    private String unionid;

    /**
     * 错误码
     */
    private int errcode;

    /**
     * 错误信息
     */
    private String errmsg;

}
