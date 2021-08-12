package cn.acyou.leo.framework.model;

/**
 * @author youfang
 * @version [1.0.0, 2020/5/13]
 **/
public interface ErrorEnum<C extends Enum<?>> {

    /**
     * 返回枚举项的 code
     *
     * @return int
     */
    int code();

    /**
     * 返回枚举项的 message
     *
     * @return {@link String}
     */
    String message();

    /**
     * 返回枚举对象
     *
     * @return {@link C}
     */
    C get();

}
