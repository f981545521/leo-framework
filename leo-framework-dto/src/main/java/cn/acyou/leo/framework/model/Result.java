package cn.acyou.leo.framework.model;

import java.io.Serializable;

/**
 * @author youfang
 * @since  2020/03/19 11:59
 **/
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MESSAGE = "处理成功";
    private static final int ERROR_CODE = 500;
    private static final String ERROR_MESSAGE = "未知异常，请联系管理员";

    public Result() {

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

    public static <T> Result<T> error(ErrorEnum<?> errorEnum) {
        return error(errorEnum.code(), errorEnum.message());
    }
    public static <T> Result<T> error(ErrorEnum<?> errorEnum, T data) {
        return error(errorEnum.code(), errorEnum.message(), data);
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
