package cn.acyou.leo.framework.wx.dto;

import lombok.Data;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-09 11:18]
 */
@Data
public class WxCommonResp {
    /**
     * 错误码 0 -ok
     */
    private Integer errcode;
    /**
     * 错误信息
     */
    private String errmsg;

    public boolean success() {
        return errcode == null || errcode == 0;
    }

    public boolean unSuccess() {
        return !success();
    }
}
