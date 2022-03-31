package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2020/9/4]
 **/
@Data
public class WeappTemplateMsg {
    /**
     * 小程序模板ID
     */
    private String template_id;
    /**
     * 小程序页面路径
     */
    private String page;
    /**
     * 小程序模板消息formid
     */
    private String form_id;
    /**
     * 小程序模板数据
     */
    private String data;
    /**
     * 小程序模板放大关键词
     */
    private String emphasis_keyword;

}
