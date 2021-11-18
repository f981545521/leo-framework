package cn.acyou.leo.framework.exception;

/**
 * 服务500异常
 * @author fangyou
 * @version [1.0.0, 2021-11-18 19:56]
 */
public class ServerInternalException extends RuntimeException {
    public ServerInternalException() {
        super();
    }

    public ServerInternalException(String message) {
        super(message);
    }
}
