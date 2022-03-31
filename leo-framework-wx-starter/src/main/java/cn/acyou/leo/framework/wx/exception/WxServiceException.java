package cn.acyou.leo.framework.wx.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 10:21]
 */
public class WxServiceException extends RuntimeException {
    public WxServiceException() {
    }

    public WxServiceException(String message) {
        super(message);
    }

    public WxServiceException(String formatPattern, Object... args) {
        super(MessageFormatter.format(formatPattern, args).getMessage());
    }
}
