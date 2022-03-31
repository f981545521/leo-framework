package cn.acyou.leo.framework.wx.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WxMpTemplateMessage implements Serializable {
    private static final long serialVersionUID = 5063374783759519418L;

    /**
     * 用户openid.
     * 可以是小程序的openid，也可以是mp_template_msg.appid对应的公众号的openid
     */
    private String touser;
    /**
     * 公众号模板id.
     */
    private String template_id;
    /**
     * 公众号模板消息所要跳转的url.
     */
    private String url;

    /**
     * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系.
     */
    private MpTemplateMsg.MiniProgram miniprogram;

    /**
     * 小程序模板数据.
     */
    private Map<String, MpTemplateMsg.WxMaTemplateData> data;


    @Data
    public static class MiniProgram implements Serializable {
        private static final long serialVersionUID = 1L;

        private String appid;
        private String pagePath;

        public MiniProgram(String pagePath) {
            this.pagePath = pagePath;
        }
    }

    @Data
    @AllArgsConstructor
    public static class WxMaTemplateData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String value;
        private String color = "#173177";

        public WxMaTemplateData() {
        }

        public WxMaTemplateData(String value) {
            this.value = value;
        }
    }


    public static WxMpTemplateMessage convertUniformMessage(WxMaUniformMessage wxMaUniformMessage) {
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTouser(wxMaUniformMessage.getTouser());
        wxMpTemplateMessage.setTemplate_id(wxMaUniformMessage.getMp_template_msg().getTemplate_id());
        wxMpTemplateMessage.setUrl(wxMaUniformMessage.getMp_template_msg().getUrl());
        wxMpTemplateMessage.setMiniprogram(wxMaUniformMessage.getMp_template_msg().getMiniprogram());
        wxMpTemplateMessage.setData(wxMaUniformMessage.getMp_template_msg().getData());
        return wxMpTemplateMessage;
    }

}
