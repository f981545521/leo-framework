package cn.acyou.leo.framework.constant;

import cn.acyou.leo.framework.model.ErrorEnum;

/**
 * 统一错误码/错误信息
 * @author youfang
 * @version [1.0.0, 2020/7/9]
 **/
public enum CommonErrorEnum implements ErrorEnum<CommonErrorEnum> {
    /** 公共 */
    E_PARAM_ERROR(3000, "请求参数错误，请检查！"),
    E_NOT_FOUNT(3010, "请求接口不存在，请检查！"),
    /** 登录 */
    E_UNAUTHENTICATED(4001, "未登录，请先登录！"),
    E_LOGIN_TIMEOUT(4001, "登录超时，请重新登录！"),
    E_LOGIN_AT_OTHER_WHERE(4001, "账号在别处登录，已被迫下线！"),
    E_MINIPRRGRAM_FAIL(4002, "微信登录失败，请获取手机号登录！"),
    /** 权限 */
    E_UNAUTHORIZED(4003, "对不起，您没有访问权限，如需要请联系管理员！"),
    E_DATA_PERMISSION_DENIED(4003, "您没有查看数据的权限，请联系管理员！"),
    /** 二次确认 */
    E_NEED_SURE(4004, "需要确认操作！"),
    /** 要刷新页面 */
    E_DO_REFRESH(4005, "请刷新页面！"),
    E_OPTMISTIC_MODIFIED(4005, "页面内容过期了，请刷新页面后再继续操作！"),
    E_OPTMISTIC_REMOVE(4005, "页面数据已经被删除，请稍后刷新再试！"),

    E_INVALID_SORT_PARAMETER(4100, "非法的OrderBy参数，请检查！"),
    E_MAXUPLOADSIZEEXCEEDED(4200, "文件超过上传限制！"),


    E_DELETE(5001,"删除数据失败！"),
    E_ZERO(5002,"数量不能为空或者0"),
    E_NULL(5003,"当前对象不存在"),
    ACCEPTANCE_BEYOND(6001,"验收数量不能超过剩余数量"),
    APAUDITOR_SAVE(7001,"当前单据以全部确认，请返回主页"),
    E_100001(100001, "登录用户不存在，已经为您自动注册，请联系管理员激活！"),
    E_CODE_BEEN_USE(100002, "小程序登录Code已经被使用，请退出后重新登录。"),
    E_200017(200017, "当前状态不正确！"),
    ;

    // 成员变量
    private int code;

    private String message;

    CommonErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 普通方法
    public static String getMessage(int code) {
        for (CommonErrorEnum c : CommonErrorEnum.values()) {
            if (c.getCode() == code) {
                return c.message;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 返回枚举项的 code
     */
    @Override
    public int code() {
        return this.code;
    }

    /**
     * 返回枚举项的 message
     */
    @Override
    public String message() {
        return this.message;
    }

    /**
     * 返回枚举对象
     */
    @Override
    public CommonErrorEnum get() {
        return this;
    }
}
