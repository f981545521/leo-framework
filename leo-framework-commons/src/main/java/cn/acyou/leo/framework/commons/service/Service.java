package cn.acyou.leo.framework.commons.service;

import java.util.Collection;
import java.util.List;

/**
 * 顶级 Service，含有基础Mapper所有方法。
 *
 * @author youfang
 * @version [1.0.0, 2020/4/17]
 **/
public interface Service<T> {
    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record 记录
     * @return 影响行数
     */
    int insert(T record);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record 记录
     * @return 影响行数
     */
    int insertSelective(T record);

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    int insertList(List<T> recordList);

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     * 这个支持数据库默认值
     * @param recordList 记录List
     * @return 影响行数
     */
    int insertListSelective(List<T> recordList);

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record 记录
     * @return 影响行数
     */
    int delete(T record);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Object key);

    /**
     * 根据Example条件删除数据
     *
     * @param example 条件
     * @return 影响行数
     */
    int deleteByExample(Object example);

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return 影响行数
     */
    int delete(String ids);

    /**
     * 根据主键List进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 主键集合。 如 List<Long>
     * @return 查询结果
     */
    <E> List<T> selectByPrimaryKeyList(Collection<E> idList);

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 主键集合 如 List<Long>
     * @return 影响行数 影响行数
     */
    <E> int deleteByPrimaryKeyList(Collection<E> idList);

    /**
     * 批量更新（属性为null不更新）
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    int updateListSelective(Collection<T> recordList);
    /**
     * 批量更新（属性为null也更新）
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    int updateList(Collection<T> recordList);

    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record  记录
     * @param example 条件
     * @return 影响行数
     */
    int updateByExample(T record, Object example);

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @param record  记录
     * @param example 条件
     * @return 影响行数
     */
    int updateByExampleSelective(T record, Object example);

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值 （根据注解忽略）
     * {@link cn.com.zhengya.framework.mapper.tkMapper.annotation.SelectiveIgnore}
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @param example example
     * @return 影响行数
     */
    int updateByExampleSelectiveCustom(T record, Object example);

    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record 记录
     * @return 影响行数
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record 记录
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(T record);
    /**
     * 根据主键更新属性不为null的值 （根据注解忽略）
     * {@link cn.com.zhengya.framework.mapper.tkMapper.annotation.SelectiveIgnore}
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @return 影响行数
     */
    int updateByPrimaryKeySelectiveCustom(T record);
    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    List<T> select(T record);

    /**
     * 根据Example条件进行查询
     *
     * @param example 条件
     * @return 查询结果
     */
    List<T> selectByExample(Object example);

    /**
     * 查询全部结果
     *
     * @return 查询结果
     */
    List<T> selectAll();

    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return 查询结果
     */
    List<T> selectByIds(String ids);

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    int selectCount(T record);

    /**
     * 根据Example条件进行查询总数
     *
     * @param example 条件
     * @return 查询结果
     */
    int selectCountByExample(Object example);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key 主键
     * @return 查询结果
     */
    T selectByPrimaryKey(Object key);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    T selectOne(T record);

    /**
     * 根据Example条件进行查询
     *
     * @param example 条件
     * @return 查询结果
     */
    T selectOneByExample(Object example);

    /**
     * 根据属性查询
     *
     * @param propertyName 实体属性
     * @param value        v
     * @param args         参数 必须是偶数，否则忽略
     * @return 查询结果
     */
    List<T> selectByProperties(String propertyName, Object value, Object... args);
}
