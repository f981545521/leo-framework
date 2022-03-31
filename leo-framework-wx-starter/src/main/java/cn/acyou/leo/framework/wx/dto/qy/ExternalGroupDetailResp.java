package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 外部组详细职责
 *
 * @author youfang
 * @version [1.0.0, 2022/1/14 15:58]
 * @date 2022/01/14
 */
@Data
public class ExternalGroupDetailResp {

    private Integer errcode;
    private String errmsg;
    private GroupChat group_chat;

    @Data
    public static class GroupChat implements Serializable {

        private static final long serialVersionUID = 7886528835339425606L;

        private String chat_id;

        private String name;

        private String owner;

        private Integer create_time;

        private List<GroupChatMember> member_list;

        private String admin_list;
    }

    @Data
    public static class GroupChatMember implements Serializable {
        private static final long serialVersionUID = 1L;

        private String userid;
        /**
         * 成员类型。
         * 1 - 企业成员
         * 2 - 外部联系人
         */
        private int type;
        private long join_time;
        private int join_scene;
        private String invitor;
        /**
         * 在群里的昵称
         */
        private String group_nickname;
        private String name;
    }
}
