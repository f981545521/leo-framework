package cn.acyou.leo.framework.push.umeng;

import cn.acyou.leo.framework.ClientEnum;
import lombok.Data;

import java.util.Map;

/**
 * 自定义播
 */
@Data
public class CustomizedcastDTO {

    /**
     * 当前设备类型
     */
    private ClientEnum deviceType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 通知栏提示文字
     */
    private String ticker;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知文字描述
     */
    private String text;

    /**
     * 定制额外参数
     */
    Map<String, String> customField;
}
