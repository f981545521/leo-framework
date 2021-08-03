package cn.acyou.leo.framework.exception;

/**
 * 乐观锁检查："页面数据已经被删除,请稍后刷新再试!"
 * @author youfang
 * @version [1.0.0, 2020/8/5]
 **/
public class RemovedByAnotherUserException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public RemovedByAnotherUserException() {
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
    public RemovedByAnotherUserException(String message) {
        super(message);
    }
}
