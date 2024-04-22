package cn.acyou.leo.framework.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设置日志级别
 *
 * @author youfang
 * @version [1.0.0, 2024/4/22 14:55]
 **/
public class LoggerUtil {
    /**
     * 设置日志级别
     * LoggerUtil.setLevel("cn.acyou.leo.framework.util.component.TranslateUtil", "ERROR");
     *
     * @param referenceName 包路径
     * @param newLevel 日志级别
     */
    public static void setLevel(String referenceName, String newLevel) {
        setLevel(referenceName, Level.toLevel(newLevel));
    }

    /**
     * 设置日志级别
     * LoggerUtil.setLevel("cn.acyou.leo.framework.util.component.TranslateUtil", Level.ERROR);
     *
     * @param referenceName 包路径
     * @param newLevel 日志级别
     */
    public static void setLevel(String referenceName, Level newLevel) {
        Logger logger = (Logger) LoggerFactory.getLogger(referenceName);
        logger.setLevel(newLevel);
    }
}
