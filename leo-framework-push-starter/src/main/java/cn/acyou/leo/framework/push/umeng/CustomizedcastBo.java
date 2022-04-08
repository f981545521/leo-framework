package cn.acyou.leo.framework.push.umeng;

import lombok.Data;

import java.util.Map;

/**
 * 自定义播
 */
@Data
class CustomizedcastBo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否是测试模式
     */
    private boolean test;

    /**
     * 应用唯一标识
     */
    private String appkey;

    /**
     * App Master Secret
     */
    private String appMasterSecret;

    /**
     * 推送别名
     */
    private String alias;

    /**
     * 推送别名类型
     */
    private String aliasType;

    /**
     * 提示文字
     */
    private String ticker;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String text;

    /**
     * 定制额外参数
     */
    Map<String, String> customField;
}
