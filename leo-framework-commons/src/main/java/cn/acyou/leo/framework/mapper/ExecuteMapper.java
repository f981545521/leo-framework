package cn.acyou.leo.framework.mapper;

import cn.acyou.leo.framework.base.TableFields;
import cn.acyou.leo.framework.model.IdReq;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 直接执行SQL
 *
 * 已由Framework扫描   {@link cn.acyou.leo.framework.auto.LeoFrameworkConfiguration}
 * @author youfang
 * @version [1.0.0, 2024/4/22 10:35]
 **/
@Mapper
public interface ExecuteMapper {

    /**
     * 执行列表的查询，会返回别名的对应map列表（LinkedHashMap 可以保证顺序）
     *
     * @param sql sql语句
     * @return 查询结果 别名的对应map列表
     */
    @Select("${sql}")
    List<LinkedHashMap<String , Object>> executeQuerySql(@Param("sql") String sql);

    /**
     * 执行单个字段的查询，会返回一个值
     *
     * @param sql sql语句
     * @return 查询结果 会返回一个值
     */
    @Select("${sql}")
    String executeIndividualQuerySql(@Param("sql") String sql);

    /**
     * 查询表是否包含列
     * 存在为1/不存在为0
     *
     * @param tableName 表
     * @param columnName 列
     * @return 查询结果 会返回一个值
     */
    @Select("select count(1) as isExist from (\n" +
            "select\n" +
            "\tCOLUMN_NAME\n" +
            "from\n" +
            "\tINFORMATION_SCHEMA.COLUMNS\n" +
            "where\n" +
            "   TABLE_NAME = '${tableName}' and COLUMN_NAME = '${columnName}'\n" +
            ") as t")
    int columnIsExist(@Param("tableName") String tableName, @Param("columnName") String columnName);

    /**
     * 查询表是否包含列
     * 存在为1/不存在为0
     *
     * @param tableName 表
     * @return 查询结果 会返回一个值
     */
    @Select("SELECT COUNT(1) as isExist FROM information_schema.tables WHERE table_schema=database() AND table_name='${tableName}'")
    int tableIsExist(@Param("tableName") String tableName);

    /**
     * 使用`DESCRIBE`命令获取表的信息
     * @param tableName 表
     * @return 查询结果 表字段信息
     */
    @Select("DESCRIBE ${tableName}")
    List<TableFields> executeDescribe(@Param("tableName") String tableName);

    /**
     * 执行insert语句
     *
     * @param sql sql语句
     * @return int 1成功 0失败
     */
    @Insert("${sql}")
    int executeInsertSql(@Param("sql") String sql);
    /**
     * 执行insert语句
     *
     * @param sql sql语句
     * @return int 1成功 0失败
     */
    @Insert("${sql}")
    @Options(useGeneratedKeys = true, keyProperty = "idReq.id")
    int executeInsertSqlV2(@Param("sql") String sql, @Param("idReq") IdReq idReq);

    /**
     * 执行update语句
     *
     * @param sql sql语句
     * @return int 1成功 0失败
     */
    @Update("${sql}")
    int executeUpdateSql(@Param("sql") String sql);

    /**
     * 执行delete语句
     *
     * @param sql sql语句
     * @return int 1成功 0失败
     */
    @Delete("${sql}")
    int executeDeleteSql(@Param("sql") String sql);

    /**
     * 执行ddl语句 同update
     *
     * @param sql sql语句
     * @return int 1成功 0失败
     */
    @Update("${sql}")
    int executeDDLSql(@Param("sql") String sql);
}
