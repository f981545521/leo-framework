package cn.acyou.leo.framework.wx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *     example :
 *
 * {
 *     "touser":"oG6x55KAVKXoBxXUryae49yCptrQ",
 *     "mp_template_msg":{
 *         "appid":"wx9459ec209f2a64d7",
 *         "template_id":"rMMnXPeFyjimdpbRz_unSnfRe4pA871mw0udk1viyaA",
 *         "url":"http://weixin.qq.com/download",
 *         "miniprogram":{
 *             "appid":"wx6725c566a41c8208"
 *         },
 *        "data":{
 *                "first": {
 *                    "value":"单号:SD-00001！",
 *                    "color":"#173177"
 *                },
 *                "keyword1":{
 *                    "value":"高压氧舱",
 *                    "color":"#173177"
 *                },
 *                "keyword2": {
 *                    "value":"外科",
 *                    "color":"#173177"
 *                },
 *                "keyword3": {
 *                    "value":"50",
 *                    "color":"#173177"
 *                },
 *                "remark":{
 *                    "value":"请快点补货，急用！！",
 *                    "color":"#173177"
 *                }
 *        }
 *     }
 * }
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020/9/4]
 **/
@Data
public class MpTemplateMsg {

    /**
     * 公众号appid，要求与小程序有绑定且同主体.
     */
    private String appid;

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
    private MiniProgram miniprogram;

    /**
     * 小程序模板数据.
     */
    private Map<String, WxMaTemplateData> data;

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
}
