package cn.acyou.leo.tool.mapper;

import cn.acyou.leo.tool.entity.Order1;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author youfang
 * @since 2025-08-28
 */
public interface Order1Mapper {

    int save(Order1 entity);

    int saveBatch(@Param("list") List<Order1> entityList);

    int removeById(Serializable id);

    int removeByIds(Collection<?> list);

    int updateById(Order1 entity);

    int updateBatchById(@Param("list") List<Order1> entityList);

    Order1 getById(Serializable id);

    List<Order1> listByIds(Collection<? extends Serializable> idList);

}
