package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2021/2/3]
 **/
@Data
public class WxMpUserInfo {
    //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
    private int subscribe;

    private String openid;

    private String nickname;

    private int sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    private int subscribe_time;

    private String unionid;

    private String remark;

    private int groupid;

    private String tagid_list;

    private String subscribe_scene;

    private int qr_scene;

    private String qr_scene_str;
}
