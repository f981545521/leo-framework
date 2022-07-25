package cn.acyou.leo.framework.constant;

import cn.acyou.leo.framework.model.ErrorEnum;

/**
 * 统一错误码/错误信息
 * 6位数字code
 * 系统模块：10XXXX
 *
 * 用户模块：20XXXX
 * 订单模块：21XXXX
 * 商品模块：22XXXX
 *
 *
 * @author youfang
 * @version [1.0.0, 2020/7/9]
 **/
public enum CommonErrorEnum implements ErrorEnum {
    /**
     * 公共/系统
     */
    E_PARAM_ERROR(101001, "请求参数错误，请检查！"),
    E_NOT_FOUNT(101002, "请求接口不存在，请检查！"),
    E_INVALID_SORT_PARAMETER(101003, "非法的排序参数，请检查！"),
    E_MAX_UPLOAD_SIZE_EXCEEDED(101004, "文件超过上传限制！"),
    ACCESS_LIMIT(101005, "重复请求，请稍后再试！"),
    REPETITIVE_OPERATION(101015, "请勿重复操作！"),
    E_PARAM_VALID_ERROR(101006, "请求参数校验错误，请检查！"),
    CONCURRENT_ERROR(101007, "正在处理中，请稍后..."),
    BAD_SQL_ERROR(101008, "SQL语法错误，请检查！"),
    /**
     * 登录
     */
    E_UNAUTHENTICATED(102001, "未登录，请先登录！"),
    E_LOGIN_TIMEOUT(102002, "登录超时，请重新登录！"),
    E_LOGIN_AT_OTHER_WHERE(102003, "账号在别处登录，已被迫下线！"),
    E_MINI_PROGRAM_FAIL(102004, "微信登录失败，请获取手机号登录！"),
    E_CODE_BEEN_USE(102005, "小程序登录Code已经被使用，请退出后重新登录。"),
    /**
     * 权限
     */
    E_UNAUTHORIZED(103001, "对不起，您没有访问权限，如需要请联系管理员！"),
    E_DATA_PERMISSION_DENIED(103002, "您没有查看数据的权限，请联系管理员！"),
    /**
     * 二次确认
     */
    E_NEED_SURE(104001, "需要确认操作！"),
    /**
     * 要刷新页面
     */
    E_DO_REFRESH(104002, "请刷新页面！"),
    /**
     * 乐观锁
     */
    E_OPTMISTIC_MODIFIED(105001, "页面内容过期了，请刷新页面后再继续操作！"),
    E_OPTMISTIC_REMOVE(105002, "页面数据已经被删除，请稍后刷新再试！"),

    ;

    // 成员变量
    private final int code;

    private final String message;

    CommonErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 得到提示信息
     *
     * @param code 代码
     * @return 提示信息
     */
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
}
