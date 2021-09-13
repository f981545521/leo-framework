package cn.acyou.leo.framework.model;

/**
 * 错误顶级接口
 *
 * @author youfang
 * @version [1.0.0, 2020/5/13]
 **/
public interface ErrorEnum {

    /**
     * 返回枚举项的 code
     *
     * @return int
     */
    int getCode();

    /**
     * 返回枚举项的 message
     *
     * @return {@link String}
     */
    String getMessage();

}
