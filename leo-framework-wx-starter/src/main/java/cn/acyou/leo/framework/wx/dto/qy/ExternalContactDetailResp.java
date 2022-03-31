package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2022/1/14 15:58]
 **/
@Data
public class ExternalContactDetailResp {

    private Integer errcode;
    private String errmsg;
    private Contact external_contact;
    private String follow_user;

    @Data
    public static class Contact implements Serializable {

        private static final long serialVersionUID = 7886528835339425606L;

        private String external_userid;

        private String name;

        private String type;

        private String avatar;

        private String gender;

    }
}
