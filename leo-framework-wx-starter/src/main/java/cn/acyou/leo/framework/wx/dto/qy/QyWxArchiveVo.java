package cn.acyou.leo.framework.wx.dto.qy;

import lombok.Builder;
import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2022/1/14 16:16]
 **/
@Data
@Builder
public class QyWxArchiveVo {
    private long seq;
    private String msgid;
    private String from;
    //消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型。String类型
    private String action;
    //消息类型 文本消息为：text。String类型
    private String msgtype;
    private String from_name;
    private String tolist;
    private String roomid;
    private String room_name;
    private String msgtime;
    private String msgcontent;
}
