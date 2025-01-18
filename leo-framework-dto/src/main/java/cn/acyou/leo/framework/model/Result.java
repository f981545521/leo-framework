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
    private String msg;
    @ApiModelProperty("响应内容")
    private T data;


    private Result() {

    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> error() {
        return error(ApiStatusCode.ERROR.getCode(), ApiStatusCode.ERROR.getMsg());
    }

    public static <T> Result<T> error(String message) {
        return error(ApiStatusCode.ERROR.getCode(), message);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> error(String code, String message) {
        return new Result<>(Integer.parseInt(code), message);
    }

    public static <T> Result<T> error(ErrorEnum errorEnum) {
        return error(errorEnum.getCode(), errorEnum.getMsg());
    }
    public static <T> Result<T> error(ErrorEnum errorEnum, T data) {
        return error(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> success() {
        return success(ApiStatusCode.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(T data) {
        return success(ApiStatusCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ApiStatusCode.SUCCESS.getCode(), message, data);
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

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean successful() {
        return this.code == ApiStatusCode.SUCCESS.getCode();
    }

    public boolean unsuccessful() {
        return this.code != ApiStatusCode.SUCCESS.getCode();
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
                ", msg ='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
