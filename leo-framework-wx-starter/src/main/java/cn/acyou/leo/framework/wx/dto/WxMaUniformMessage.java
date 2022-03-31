package cn.acyou.leo.framework.wx.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 模板消息.
 * 参考文档说明
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/uniform-message/uniformMessage.send.html
 *
 * @author youfang
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxMaUniformMessage implements Serializable {
    private static final long serialVersionUID = 5063374783759519418L;

    /**
     * 用户openid.
     * 可以是小程序的openid，也可以是mp_template_msg.appid对应的公众号的openid
     */
    private String touser;

    /**
     * 小程序模板消息相关的信息，可以参考小程序模板消息接口; 有此节点则优先发送小程序模板消息
     * 非必填
     */
    private WeappTemplateMsg weapp_template_msg;

    /**
     * 公众号模板消息相关的信息，可以参考公众号模板消息接口；有此节点并且没有weapp_template_msg节点时，发送公众号模板消息
     * 必填
     */
    private MpTemplateMsg mp_template_msg;

}
