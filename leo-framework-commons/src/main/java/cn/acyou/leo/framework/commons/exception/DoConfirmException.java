package cn.acyou.leo.framework.commons.exception;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/29]
 **/
public class DoConfirmException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public DoConfirmException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DoConfirmException(String message) {
        super(message);
    }
}
