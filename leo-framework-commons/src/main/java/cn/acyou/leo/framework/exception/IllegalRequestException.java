package cn.acyou.leo.framework.exception;

/**
 * 非法请求
 *
 * 表示请求走到这里不合逻辑（疑似黑客攻击）
 * @author fangyou
 * @version [1.0.0, 2021-11-19 13:57]
 */
public class IllegalRequestException extends RuntimeException{
    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(String message) {
        super(message);
    }
}
