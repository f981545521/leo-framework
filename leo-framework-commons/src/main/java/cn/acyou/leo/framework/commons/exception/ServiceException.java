package cn.acyou.leo.framework.commons.exception;

import cn.acyou.leo.framework.commons.constant.ErrorEnum;
import cn.acyou.leo.framework.commons.model.Result;
import org.slf4j.helpers.MessageFormatter;

/**
 * 服务异常
 *
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 * @since [公共服务]
 **/
public class ServiceException extends RuntimeException {

    private Result<Object> result;

    public Result<Object> getResult() {
        return result;
    }

    public void setResult(Result<Object> result) {
        this.result = result;
    }

    public ServiceException(ErrorEnum<?> errorEnum){
        super(errorEnum.message());
        this.result = Result.error(errorEnum.code(), errorEnum.message());
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ServiceException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * 格式化Message，同log.info()的格式化。
     * <pre>
     *     example:
     *     throw new ServiceException("hello, i'm {}, age {}.", "james", 10); message-> hello, i'm james, age 10.
     * </pre>
     * @param formatPattern 格式化模板字符串
     * @param args 参数
     */
    public ServiceException(String formatPattern, Object... args){
        super(MessageFormatter.arrayFormat(formatPattern, args).getMessage());
    }

    /**
     * 携带result 的异常
     *
     * @param result result信息
     */
    public ServiceException(Result<Object> result) {
        super(result.getMessage());
        this.result = result;
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail
     * message, cause, suppression enabled or disabled, and writable
     * stack trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @since 1.7
     */
    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
