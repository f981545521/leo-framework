package cn.acyou.leo.framework.mapper.tkMapper.provide;

import cn.acyou.leo.framework.mapper.tkMapper.util.TkSqlHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Set;

/**
 * CommonMapperProvider实现类，自己添加的公共方法Mapper
 *
 * @author youfang
 */
public class CommonMapperProvider extends MapperTemplate {

    public CommonMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 查询当前记录数 + 1
     *
     * 实体中必须有@Id注解
     */
    public String getNextSortNumber(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append("SELECT count(").append(column.getColumn()).append(") + 1 ");
            sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
            return sql.toString();
        } else {
            throw new MapperException("继承 getNextSortNumber 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }

    }
    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     */
    public String selectByPrimaryKeyList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\" >");
            sql.append("#{id}");
            sql.append("</foreach>");
        } else {
            throw new MapperException("继承 selectByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        return sql.toString();
    }
    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     */
    public String deleteByPrimaryKeyList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\" >");
            sql.append("#{id}");
            sql.append("</foreach>");
        } else {
            throw new MapperException("继承 deleteByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        return sql.toString();
    }
    /**
     * 根据主键批量更新
     */
    public String updateListSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append("<if test=\"list==null or list.size()==0\">");
            sql.append("    select 0 from dual");
            sql.append("</if>");
            sql.append("<foreach collection=\"list\" item=\"it\">");
            sql.append("    update " + tableName(entityClass));
            sql.append(SqlHelper.updateSetColumns(entityClass, "it", true, isNotEmpty()));
            sql.append("    WHERE " + column.getColumn() + " = #{it." + column.getProperty() + "};");
            sql.append("</foreach>");
        } else {
            throw new MapperException("继承 updateListSelective 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        return sql.toString();
    }
    /**
     * 根据主键批量更新
     */
    public String updateList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append("<if test=\"list==null or list.size()==0\">");
            sql.append("    select 0 from dual");
            sql.append("</if>");
            sql.append("<foreach collection=\"list\" item=\"it\">");
            sql.append("    update " + tableName(entityClass));
            sql.append(SqlHelper.updateSetColumns(entityClass, "it", false, false));
            sql.append("    WHERE " + column.getColumn() + " = #{it." + column.getProperty() + "};");
            sql.append("</foreach>");
        } else {
            throw new MapperException("继承 updateList 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        return sql.toString();
    }

    /**
     * 根据主键更新属性不为null的值 （根据注解忽略）
     *
     * @return
     */
    public String updateByPrimaryKeySelectiveCustom(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(TkSqlHelper.updateCustomSetColumns(entityClass, null, true, isNotEmpty()));
        sql.append(SqlHelper.wherePKColumns(entityClass, true));
        return sql.toString();
    }

    public String insertIgnoreSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        processKey(sql, entityClass, ms, columnList);
        sql.append(TkSqlHelper.insertIgnoreIntoTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (column.isIdentity()) {
                sql.append(column.getColumn() + ",");
            } else {
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList){
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //先处理cache或bind节点
        for (EntityColumn column : columnList) {
            if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && "JDBC".equals(column.getGenerator())) {
                        continue;
                    }
                    throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                SelectKeyHelper.newSelectKeyMappedStatement(ms, column, entityClass, isBEFORE(), getIDENTITY(column));
                hasIdentityKey = true;
            }
        }
    }
    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     */
    public String deleteLogicByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(TkSqlHelper.deleteLogicSetColumns(entityClass));
        sql.append(SqlHelper.wherePKColumns(entityClass, true));
        return sql.toString();
    }
    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     */
    public String deleteLogicByPrimaryKeyList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(TkSqlHelper.deleteLogicSetColumns(entityClass));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\" >");
            sql.append("#{id}");
            sql.append("</foreach>");
        } else {
            throw new MapperException("继承 deleteLogicByPrimaryKeyList 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        return sql.toString();
    }

    /**
     * 根据Example更新非null字段
     *
     * @param ms
     * @return
     */
    public String updateByExampleSelectiveCustom(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //安全更新，Example 必须包含条件
        if (getConfig().isSafeUpdate()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("example"));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(TkSqlHelper.updateSetColumnsIgnoreVersion(entityClass, "record", true, isNotEmpty()));
        sql.append(SqlHelper.updateByExampleWhereClause());
        return sql.toString();
    }



    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertListSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>");
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass), "list[0]"));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(TkSqlHelper.getIfNotNull("record", column, column.getColumnHolder("record") + ",", false));
                sql.append(TkSqlHelper.getIfIsNull("record", column, "default" + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        // 反射把MappedStatement中的设置主键名
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);
        return sql.toString();
    }
    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertIgnoreListSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>");
        sql.append(TkSqlHelper.insertIgnoreIntoTable(entityClass, tableName(entityClass), "list[0]"));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(TkSqlHelper.getIfNotNull("record", column, column.getColumnHolder("record") + ",", isNotEmpty()));
                sql.append(TkSqlHelper.getIfIsNull("record", column, "default" + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        // 反射把MappedStatement中的设置主键名
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);
        return sql.toString();
    }
    /**
     * 插入，主键id，自增
     *
     * @param ms
     */
    public String insertUseGeneratedKeys(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(SqlHelper.insertValuesColumns(entityClass, true, false, false));

        // 反射把MappedStatement中的设置主键名
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);

        return sql.toString();
    }


}
