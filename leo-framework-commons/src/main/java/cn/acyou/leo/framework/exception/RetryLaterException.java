package cn.acyou.leo.framework.exception;

import cn.acyou.leo.framework.model.ErrorEnum;
import cn.acyou.leo.framework.model.Result;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/28 11:13]
 **/
public class RetryLaterException extends ServiceException {
    public RetryLaterException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public RetryLaterException() {
    }

    public RetryLaterException(String message) {
        super(message);
    }

    public RetryLaterException(String formatPattern, Object... args) {
        super(formatPattern, args);
    }

    public RetryLaterException(Result<Object> result) {
        super(result);
    }

    public RetryLaterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryLaterException(Throwable cause) {
        super(cause);
    }

    public RetryLaterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
