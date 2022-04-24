package cn.acyou.leo.framework.commons;

/**
 * @author youfang
 * @version [1.0.0, 2020/8/17]
 **/
public class RedisKeyConstant {
    /**登录用户（根据用户TOKEN查询用户ID） TOKEN:USERID */
    public static final String USER_LOGIN_TOKEN = "USER:LOGIN:TOKEN:";
    /**
     * 登录用户（根据用户ID查询TOKEN） USERID:TOKEN
     */
    public static final String USER_LOGIN_ID = "USER:LOGIN:ID:";
    /**
     * 登录用户（根据用户ID查询登录信息） USERID:LOGINUSER
     */
    public static final String USER_LOGIN_INFO = "USER:LOGIN:USER:";
    /**
     * 用户登录锁定
     */
    public static final String USER_LOGIN_LOCK = "USER:LOGIN:LOCK:";
    /**
     * 用户在其他地方登录
     */
    public static final String USER_LOGIN_AT_OTHER_WHERE = "USER:LOGIN:LOGINATOTHERWHERE:";
    /**
     * 接口统计分析KEY
     */
    public static final String INTERFACE_CALL_STATISTICS = "STATISTICS:INTERFACE_CALL";
    /**
     * 接口强幂等
     */
    public static final String AUTO_IDEMPOTENT_SEQUENCE = "LEO:AUTO_IDEMPOTENT_SEQUENCE:";

}
