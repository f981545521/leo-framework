package cn.acyou.leo.framework.service;

import cn.acyou.leo.framework.annotation.mapper.SelectiveIgnore;
import cn.acyou.leo.framework.mapper.Mapper;
import cn.acyou.leo.framework.mapper.tkMapper.util.TkSqlHelper;
import cn.acyou.leo.framework.util.ReflectUtils;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 顶级 Service 实现
 *
 * @author youfang
 * @version [1.0.0, 2020/4/17]
 **/
//事务已经通过{@link cn.com.zhengya.spdapi.conf.TransactionAdviceConfig} 配置AOP事务
//@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class ServiceImpl<M extends Mapper<T>, T> implements Service<T> {

    private final static Logger log = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    protected M baseMapper;

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public ServiceImpl() {
        TypeToken<T> poType = new TypeToken<T>(getClass()) {};
        clazz = (Class<T>) poType.getRawType();
    }

    /**
     * 构建查询Example
     * <pre>
     *          Example example = builderExample()
     *              .where(Sqls.custom()
     *                      .andGreaterThan("birth", DateUtil.parseDate("2020-04-01")))
     *              .orderByDesc("age")
     *              .build();
     *         List<Student> students = baseMapper.selectByExample(example);
     * </pre>
     * <p>
     * 使用example的setOrderByClause方法需要是数据库的字段。   如：example.setOrderByClause("user_name asc");
     * 使用builderExample的方法需要时propertyName。           如：builderExample().orderByDesc("userName")
     *
     * @return 参数构造器
     */
    public Example.Builder builderExample() {
        return Example.builder(clazz);
    }

    /**
     * 根据Sqls构建Example
     *
     * @param sqls sqls
     * @return {@link Example}
     */
    public Example buildWhereSqlsExample(Sqls sqls) {
        return builderExample().where(sqls).build();
    }
    /**
     * 根据Sqls构建Example OrderByAsc
     *
     * @param sqls sqls
     * @return {@link Example}
     */
    public Example buildOrderByAscWhereSqlsExample(Sqls sqls, String orderByProperties) {
        return builderExample().orderByAsc(orderByProperties).where(sqls).build();
    }
    /**
     * 根据Sqls构建Example OrderByDesc
     *
     * @param sqls sqls
     * @return {@link Example}
     */
    public Example buildOrderByDescWhereSqlsExample(Sqls sqls, String orderByProperties) {
        return builderExample().orderByDesc(orderByProperties).where(sqls).build();
    }


    /*
     * ————————————————————————————         INSERT   ——————————————————————————————————————————
     */

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int insert(T record) {
        return baseMapper.insert(record);
    }

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int insertSelective(T record) {
        return baseMapper.insertSelective(record);
    }
    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int insertOrUpdateByPkSelective(T record) {
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(clazz);
        if (pkColumns.size() == 1) {
            EntityColumn column = pkColumns.iterator().next();
            EntityField entityField = column.getEntityField();
            try {
                Object value = entityField.getValue(record);
                if (value != null) {
                    return baseMapper.updateByPrimaryKeySelective(record);
                }else {
                    return baseMapper.insertSelective(record);
                }
            }catch (Exception e) {
                throw new MapperException("未知异常！" + e.getMessage());
            }
        } else {
            throw new MapperException("insertOrUpdateByPkSelective 方法的实体类[" + clazz.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    @Override
    public int insertList(List<T> recordList) {
        return baseMapper.insertList(recordList);
    }

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     * 这个支持数据库默认值
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    @Override
    public int insertListSelective(List<T> recordList) {
        return baseMapper.insertListSelective(recordList);
    }

    /*
     * ————————————————————————————         UPDATE   ——————————————————————————————————————————
     */

    /**
     * 批量更新（属性为null不更新）
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    @Override
    public int updateListSelective(Collection<T> recordList) {
        return baseMapper.updateListSelective(recordList);
    }
    /**
     * 批量更新（属性为null也更新）
     *
     * @param recordList 记录List
     * @return 影响行数
     */
    @Override
    public int updateList(Collection<T> recordList) {
        return baseMapper.updateList(recordList);
    }

    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record  记录
     * @param example 条件
     * @return 影响行数
     */
    @Override
    public int updateByExample(T record, Object example) {
        return baseMapper.updateByExample(record, example);
    }

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @param record  记录
     * @param example 条件
     * @return 影响行数
     */
    @Override
    public int updateByExampleSelective(T record, Object example) {
        return baseMapper.updateByExampleSelective(record, example);
    }

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值 （根据注解忽略）
     * {@link SelectiveIgnore}
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @param example example
     * @return 影响行数
     */
    @Override
    public int updateByExampleSelectiveCustom(T record, Object example) {
        return baseMapper.updateByExampleSelectiveCustom(record, example);
    }

    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int updateByPrimaryKey(T record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int updateByPrimaryKeySelective(T record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    /**
     * 根据主键更新属性不为null的值 （根据注解忽略）{@link SelectiveIgnore}
     *
     *     在属性字段上加上注解，不会忽略null值
     *     - @SelectiveIgnore
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int updateByPrimaryKeySelectiveCustom(T record) {
        return baseMapper.updateByPrimaryKeySelectiveCustom(record);
    }

    /*
     * ————————————————————————————         DELETE   ——————————————————————————————————————————
     */

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 主键集合 如 List<Long>
     * @return 影响行数 影响行数
     */
    @Override
    public <E> int deleteByPrimaryKeyList(Collection<E> idList) {
        return baseMapper.deleteByPrimaryKeyList(idList);
    }

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record 记录
     * @return 影响行数
     */
    @Override
    public int delete(T record) {
        return baseMapper.delete(record);
    }

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key 主键
     * @return 影响行数
     */
    @Override
    public int deleteByPrimaryKey(Object key) {
        return baseMapper.deleteByPrimaryKey(key);
    }

    /**
     * 根据Example条件删除数据
     *
     * @param example 条件
     * @return 影响行数
     */
    @Override
    public int deleteByExample(Object example) {
        return baseMapper.deleteByExample(example);
    }

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return 影响行数
     */
    @Override
    public int delete(String ids) {
        return baseMapper.deleteByIds(ids);
    }

    /*
     * ————————————————————————————        SELECT   ——————————————————————————————————————————
     */

    /**
     * 根据主键List进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param idList 主键集合。 如 List<Long>
     * @return 查询结果
     */
    @Override
    public <E> List<T> selectByPrimaryKeyList(Collection<E> idList) {
        return baseMapper.selectByPrimaryKeyList(idList);
    }

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    @Override
    public List<T> select(T record) {
        return baseMapper.select(record);
    }

    /**
     * 根据Example条件进行查询
     *
     * @param example 条件
     * @return 查询结果
     */
    @Override
    public List<T> selectByExample(Object example) {
        return baseMapper.selectByExample(example);
    }

    /**
     * 查询全部结果
     *
     * @return 查询结果
     */
    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return 查询结果
     */
    @Override
    public List<T> selectByIds(String ids) {
        return baseMapper.selectByIds(ids);
    }

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    @Override
    public int selectCount(T record) {
        return baseMapper.selectCount(record);
    }

    /**
     * 根据Example条件进行查询总数
     *
     * @param example 条件
     * @return 查询结果
     */
    @Override
    public int selectCountByExample(Object example) {
        return baseMapper.selectCountByExample(example);
    }

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key 主键
     * @return 查询结果
     */
    @Override
    public T selectByPrimaryKey(Object key) {
        return baseMapper.selectByPrimaryKey(key);
    }

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record 记录
     * @return 查询结果
     */
    @Override
    public T selectOne(T record) {
        return baseMapper.selectOne(record);
    }

    /**
     * 根据Example条件进行查询
     *
     * @param example 条件
     * @return 查询结果
     */
    @Override
    public T selectOneByExample(Object example) {
        return baseMapper.selectOneByExample(example);
    }

    /**
     * 根据属性查询
     *
     * @param propertyName 实体属性
     * @param value        v
     * @param args         参数 必须是偶数，否则忽略
     * @return 查询结果
     */
    @Override
    public List<T> selectByProperties(String propertyName, Object value, Object... args) {
        if (StringUtils.isEmpty(propertyName)) {
            return new ArrayList<>();
        }
        Sqls sqls = Sqls.custom();
        sqls.andEqualTo(propertyName, value);
        int groupLen = 2;
        if (args != null && args.length % groupLen == 0) {
            for (int i = 0; i < args.length; i = i + groupLen) {
                sqls.andEqualTo(String.valueOf(args[i]), args[i + 1]);
            }
        }
        Example example = builderExample()
                .where(sqls)
                .build();
        return baseMapper.selectByExample(example);
    }
}
