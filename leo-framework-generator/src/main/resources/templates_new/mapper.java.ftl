package ${package.Mapper};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} {
    /**
    * 批量插入
    * @param entityList 实体列表
    * @return 影响行数
    */
    int insertBatch(@Param("list") List<${entity}> entityList);

    /**
    * 批量更新（根据主键）
    * @param entityList 实体列表
    * @return 影响行数
    */
    int updateBatch(@Param("list") List<${entity}> entityList);
}
</#if>
