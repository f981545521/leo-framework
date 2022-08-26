package cn.acyou.leo.framework.prop;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:26]
 **/
@Data
public class DingTalkRobot implements Serializable {

    /**
     * 机器人Hook地址
     */
    private String robotHookUrl;

    /**
     * 机器人Hook密钥
     */
    private String robotSecret;
    /**
     * 是否@所有人
     */
    private Boolean atAll;
}
