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
public class ExternalGroupListResp {

    private Integer errcode;
    private String errmsg;
    private List<GroupChatList> group_chat_list;
    private String next_cursor;

    @Data
    public static class GroupChatList implements Serializable {

        private static final long serialVersionUID = 7886528835339425606L;

        private String chat_id;

        /**
         * 这里接口返回为空，需要手动查询
         */
        private String chat_name;

        private Integer status;

    }
}
