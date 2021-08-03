package cn.acyou.leo.framework.service;

/**
 * @author youfang
 * @version [1.0.0, 2020/11/17]
 **/
public interface DictValidService {
    /**
     * 有效的dict类型值
     *
     * @param dictCode 字典类型代码
     * @param value    价值
     * @return boolean
     */
    boolean validDictValue(String dictCode, String value);
}
