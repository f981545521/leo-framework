package cn.acyou.leo.framework.mapper;

import cn.acyou.leo.framework.model.IdReq;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 注意使用的时候请使用多包扫描
 * <p>
 * ## @MapperScan({"cn.acyou.leo.tool.mapper", "cn.acyou.leo.framework.mapper"})
 * <p>
 *
 * <b>注意使用的时候请使用多包扫描:</b>
 * <pre>
 * &#064;MapperScan({"cn.acyou.leo.tool.mapper", "cn.acyou.leo.framework.mapper"})
 * </pre>
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
