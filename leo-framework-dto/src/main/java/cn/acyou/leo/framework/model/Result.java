package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.model.base.DTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author youfang
 * @since 2020/03/19 11:59
 **/
public class Result<T> extends DTO {
    @ApiModelProperty("编码(默认成功：200)")
    private int code;
    @ApiModelProperty("描述")
    private String message;
    @ApiModelProperty("响应内容")
    private T data;

    private static int SUCCESS_CODE = 200;
    private static String SUCCESS_MESSAGE = "处理成功";
    private static int ERROR_CODE = 500;
    private static String ERROR_MESSAGE = "服务繁忙，请稍后再试";

    public static void setDefaultSuccess(int SUCCESS_CODE, String SUCCESS_MESSAGE) {
        Result.SUCCESS_CODE = SUCCESS_CODE;
        Result.SUCCESS_MESSAGE = SUCCESS_MESSAGE;
    }

    public static void setDefaultError(int ERROR_CODE, String ERROR_MESSAGE) {
        Result.ERROR_CODE = ERROR_CODE;
        Result.ERROR_MESSAGE = ERROR_MESSAGE;
    }

    private Result() {

    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> error() {
        return error(ERROR_CODE, ERROR_MESSAGE);
    }

    public static <T> Result<T> error(String message) {
        return error(ERROR_CODE, message);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> error(String code, String message) {
        return new Result<>(Integer.parseInt(code), message);
    }

    public static <T> Result<T> error(ErrorEnum errorEnum) {
        return error(errorEnum.getCode(), errorEnum.getMessage());
    }
    public static <T> Result<T> error(ErrorEnum errorEnum, T data) {
        return error(errorEnum.getCode(), errorEnum.getMessage(), data);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> success() {
        return success(SUCCESS_MESSAGE, null);
    }

    public static <T> Result<T> success(T data) {
        return success(SUCCESS_MESSAGE, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    public static <T> Result<T> custom(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean successful() {
        return this.code == SUCCESS_CODE;
    }

    public boolean unsuccessful() {
        return this.code != SUCCESS_CODE;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
