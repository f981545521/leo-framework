package cn.acyou.leo.framework.mapper.tkMapper;

import cn.acyou.leo.framework.annotation.mapper.LogicDelete;
import cn.acyou.leo.framework.annotation.mapper.SelectiveIgnore;
import cn.acyou.leo.framework.mapper.tkMapper.provide.CommonMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

/**
 * 公共Mapper接口
 *
 * @param <T> 不能为空
 * @author youfang
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface CommonMapper<T> {

    /**
     * 获取下一个排序值
     * <p>
     * 实体中必须有@Id注解
     *
     * @return 下一个排序值（主键count + 1）
     */
    @SelectProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    Integer getNextSortNumber();
    /**
     * 根据主键List进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 如 List<Long>
     * @return 根据主键ID查询
     */
    @SelectProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    <E> List<T> selectByPrimaryKeyList(Collection<E> idList);
    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 如 List<Long>
     * @return 影响行数
     */
    @DeleteProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    <E> int deleteByPrimaryKeyList(Collection<E> idList);

    /**
     * 批量更新
     *
     * @param recordList 修改记录
     */
    @UpdateProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int updateListSelective(Collection<T> recordList);
    /**
     * 批量更新
     *
     * @param recordList 修改记录
     */
    @UpdateProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int updateList(Collection<T> recordList);

    /**
     * 根据主键更新属性不为null的值 （根据注解忽略）
     * {@link SelectiveIgnore}
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @return 影响行数
     */
    @UpdateProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelectiveCustom(T record);

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值 （根据注解忽略）
     * {@link SelectiveIgnore}
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @param example example
     * @return 影响行数
     */
    @UpdateProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int updateByExampleSelectiveCustom(@Param("record") T record, @Param("example") Object example);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     * insert ignore into
     * @param record 记录
     * @return 影响行数
     */
    @InsertProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int insertIgnoreSelective(T record);
    /**
     * 根据主键字段进行逻辑删除，方法参数必须包含完整的主键属性，类中只有存在一个带有{@link LogicDelete}注解的字段
     *
     * @param key 如 Long
     * @return 影响行数
     */
    @DeleteProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int deleteLogicByPrimaryKey(Object key);

    /**
     * 根据主键字段进行批量逻辑删除，方法参数必须包含完整的主键属性，类中只有存在一个带有{@link LogicDelete}注解的字段
     *
     * @param idList 如 List<Long>
     * @return 影响行数
     */
    @DeleteProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int deleteLogicByPrimaryKeyList(Collection<?> idList);

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     * null 时使用数据库默认值
     * @param recordList 记录
     * @return 影响行数
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int insertListSelective(Collection<? extends T> recordList);
    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     * null 时使用数据库默认值
     *
     * @param recordList 记录
     * @return 影响行数
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = CommonMapperProvider.class, method = "dynamicSQL")
    int insertIgnoreListSelective(Collection<? extends T> recordList);

}