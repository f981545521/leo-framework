package cn.acyou.leo.framework.exception;

/**
 * 文件操作异常
 * @author fangyou
 * @version [1.0.0, 2021-12-10 11:26]
 */
public class FsException extends RuntimeException{
    public FsException() {
    }

    public FsException(String message) {
        super(message);
    }
}
