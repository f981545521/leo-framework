package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Data;

import java.io.Serializable;


/**
 * QyUserDetailResp2
 *
 * @version [1.0.0, 2022-01-17 14:35:35]
 */
@Data
public class QyUserDetailResp implements Serializable {

    private static final long serialVersionUID = -1222961864440146366L;

    private int errcode;

    private String errmsg;

    private String userid;

    private String name;

    private String department;

    private String position;

    private String mobile;

    private String gender;

    private String email;

    private String avatar;

    private String status;

    private String isleader;

    private String extattr;

    private String telephone;

    private String enable;

    private String hide_mobile;

    private String order;

    private String external_profile;

    private String main_department;

    private String qr_code;

    private String alias;

    private String is_leader_in_dept;

    private String address;

    private String thumb_avatar;

    private String direct_leader;

}