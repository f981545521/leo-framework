package ${package.Mapper};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
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

    int save(${entity} entity);

    int saveBatch(@Param("list") List<${entity}> entityList);

    int removeById(Serializable id);

    int removeByIds(Collection<?> list);

    int updateById(${entity} entity);

    int updateBatchById(@Param("list") List<${entity}> entityList);

    ${entity} getById(Serializable id);

    List<${entity}> listByIds(Collection<? extends Serializable> idList);

}
</#if>
