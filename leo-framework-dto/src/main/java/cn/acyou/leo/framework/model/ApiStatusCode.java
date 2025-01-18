package cn.acyou.leo.framework.model;

import lombok.Getter;

/**
 * @author youfang
 * @version [1.0.0, 2025/1/17 16:43]
 **/
@Getter
public enum ApiStatusCode implements ErrorEnum {

    SUCCESS(200, "处理成功"),
    ERROR(500, "服务繁忙，请稍后再试");

    private int code;
    private String msg;

    ApiStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static void setDefaultSuccess(int SUCCESS_CODE, String SUCCESS_MSG) {
        ApiStatusCode.SUCCESS.code = SUCCESS_CODE;
        ApiStatusCode.SUCCESS.msg = SUCCESS_MSG;
    }

    public static void setDefaultError(int ERROR_CODE, String ERROR_MSG) {
        ApiStatusCode.ERROR.code = ERROR_CODE;
        ApiStatusCode.ERROR.msg = ERROR_MSG;
    }

}
