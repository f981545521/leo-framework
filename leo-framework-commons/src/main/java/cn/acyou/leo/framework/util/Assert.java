package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.exception.AssertException;

/**
 * 断言
 * Reference {@link org.springframework.util.Assert}
 *
 * @author youfang
 * @version [1.0.0, 2020-7-11 下午 04:13]
 **/
public class Assert {

    /**
     * 如果这是真的，抛出异常
     *
     * @param expression 表达式
     * @param message    消息
     */
    public static void ifTrue(boolean expression, String message) {
        if (expression) {
            throw new AssertException(message);
        }
    }

    /**
     * 如果这不是真的，抛出异常
     *
     * @param expression 表达式
     * @param message    消息
     */
    public static void ifFalse(boolean expression, String message) {
        if (!expression) {
            throw new AssertException(message);
        }
    }

    /**
     * 断言对象不是 {@code null}。
     *
     * @param object  检查对象
     * @param message 断言失败时使用的异常消息
     * @throws AssertException 如果对象是 {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AssertException(message);
        }
    }

    /**
     * 断言字符串不是 {@code null}。
     *
     * @param str     检查对象
     * @param message 断言失败时使用的异常消息
     * @throws AssertException 如果对象是 {@code null}
     */
    public static void notNull(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new AssertException(message);
        }
    }

    /**
     * 断言对象是 {@code null}。
     *
     * @param object  检查对象
     * @param message 断言失败时使用的异常消息
     * @throws AssertException 如果对象不是 {@code null}
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new AssertException(message);
        }
    }

    /**
     * 为空
     *
     * @param object 对象
     */
    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    /**
     * 非空
     *
     * @param object 对象
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

}
