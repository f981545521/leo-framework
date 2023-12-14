package cn.acyou.leo.framework.constant;

import cn.acyou.leo.framework.base.EnumEntity;
import cn.acyou.leo.framework.commons.EnumTool;

/**
 * 通用常量类
 *
 * @author youfang
 * @version [1.0.0, 2020/4/1]
 **/
public class Constant {
    /*  ============== 通用 0/1 =================== */
    public static final Integer FLAG_FALSE_0 = 0;
    public static final Integer FLAG_TRUE_1 = 1;

    /*  ============= 通用 Integer 常量 ============ */
    public static final Integer CONS_0 = 0;
    public static final Integer CONS_1 = 1;
    public static final Integer CONS_2 = 2;
    public static final Integer CONS_3 = 3;
    public static final Integer CONS_4 = 4;
    public static final Integer CONS_5 = 5;
    public static final Integer CONS_6 = 6;
    public static final Integer CONS_7 = 7;
    public static final Integer CONS_8 = 8;
    public static final Integer CONS_9 = 9;
    public static final Integer CONS_99 = 99;
    /*  ============ 通用 Long 常量 ================ */
    public static final Long CONS_LONG_0 = 0L;
    public static final Long CONS_LONG_1 = 1L;
    public static final Long CONS_LONG_2 = 2L;
    public static final Long CONS_LONG_3 = 3L;
    public static final Long CONS_LONG_4 = 4L;
    public static final Long CONS_LONG_5 = 5L;
    public static final Long CONS_LONG_6 = 6L;
    public static final Long CONS_LONG_7 = 7L;
    public static final Long CONS_LONG_8 = 8L;
    public static final Long CONS_LONG_9 = 9L;

    /*  ============== 通用常量 ==================== */
    public static final Integer NORMAL = 1;
    public static final Integer ENABLE = 1;
    public static final Integer DISABLED = 0;

    /*  ============= 通用 int 常量 ================ */
    public static class INT {
        public static final int CONS_0 = 0;
        public static final int CONS_1 = 1;
        public static final int CONS_2 = 2;
        public static final int CONS_3 = 3;
        public static final int CONS_4 = 4;
        public static final int CONS_5 = 5;
        public static final int CONS_6 = 6;
        public static final int CONS_7 = 7;
        public static final int CONS_8 = 8;
        public static final int CONS_9 = 9;
        public static final int CONS_10 = 10;
        public static final int CONS_20 = 20;
        public static final int CONS_30 = 30;
        public static final int CONS_40 = 40;
        public static final int CONS_50 = 50;
        public static final int CONS_60 = 60;
        public static final int CONS_70 = 70;
        public static final int CONS_80 = 80;
        public static final int CONS_90 = 90;
        public static final int CONS_100 = 100;
    }

    public static final String ALL = "debug_printRequestBody_printResponseBody";
    public static final String DEBUG = "debug";
    public static final String REQ = "printRequestBody";
    public static final String RESP = "printResponseBody";

    /**
     * 请求头header名称
     */
    public static final String TOKEN_NAME = "Authentication";
    /**
     * 幂等序列参数
     */
    public static final String AUTO_IDEMPOTENT_SEQUENCE = "sequence";

    /**
     * 系统角色
     */
    public static class SysRole {
        public static final String SUPER_ADMIN = "super_admin";

        public static final EnumEntity SUPER_ADMIN_ENTITY = new EnumEntity(SUPER_ADMIN, "超级管理员");

        public static EnumTool tool() {
            return EnumTool.newInstance(SysRole.class);
        }
    }
}

