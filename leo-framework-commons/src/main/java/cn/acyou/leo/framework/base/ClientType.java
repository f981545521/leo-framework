package cn.acyou.leo.framework.base;

/**
 * 客户端类型
 * @author youfang
 * @version [1.0.0, 2020/7/8]
 **/
public enum ClientType {

    /**
     * WEB 后台管理
     */
    WEB_MANAGER(1, "WEB_MANAGER"),
    /**
     * 微信小程序
     */
    MINI_PROGRAM(2, "MINI_PROGRAM"),
    /**
     * 安卓
     */
    ANDROID(3, "ANDROID"),
    /**
     * 未知的
     */
    UNKNOWN(99, "UNKNOWN")

    ;

    // 成员变量
    private final int code;

    private final String message;

    ClientType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 获取已知的code 未知的code以null表示
     *
     * @return {@link Integer}
     */
    public Integer getKnownCodeOrNull() {
        if (this.code == UNKNOWN.code){
            return null;
        }
        return this.code;
    }
}
