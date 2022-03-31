package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业微信小程序响应信息
 *
 * @author youfang
 * @date 2020/07/09
 */
@Data
public class QyWxCode2SessionResp implements Serializable {
    private static final long serialVersionUID = -1430730556893870658L;

    /**
     * 用户所属企业的corpid
     */
    private String corpid;

    /**
     * 用户在企业内的UserID，对应管理端的帐号，企业内唯一。注意：如果该企业没有关联该小程序，则此处返回加密的userid
     */
    private String userid;

    /**
     * 会话密钥
     */
    private String session_key;

    private int errcode;

    private String errmsg;

}
