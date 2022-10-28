package cn.acyou.leo.framework.wx.dto.miniprogram;

import lombok.Data;

import java.io.Serializable;


/**
 * WxGenerateUrlLinkResp
 *
 * @version [1.0.0, 2022-10-28 13:51:22]
 */
@Data
public class WxGenerateUrlLinkResp implements Serializable {

    private static final long serialVersionUID = 3421081781348954424L;

    private String errcode;

    private String errmsg;

    private String url_link;

}
