package cn.acyou.leo.framework.wx.dto.mp;

import lombok.Data;

import java.util.List;

/**
 * 获取微信公众号关注的用户
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-09 16:23]
 * @date 2021/08/09
 */
@Data
public class WxMpUsers {
    /**
     * 总粉丝量
     */
    private Integer total;
    /**
     * 这次获取的粉丝数量
     */
    private Integer count;
    /**
     * 粉丝列表
     */
    private UsersData data;
    /**
     * 拉取列表最后一个用户的openid
     */
    private String next_openid;

    @Data
    static class UsersData {
        private List<String> openid;
    }
}
