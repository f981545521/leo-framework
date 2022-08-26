package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 14:02]
 **/
@Data
@ConfigurationProperties(prefix = "leo.tool.ding-talk")
public class DingTalkProperty {

    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 机器人列表
     */
    private Map<String, DingTalkRobot> dingTalkRobotMap;

}
