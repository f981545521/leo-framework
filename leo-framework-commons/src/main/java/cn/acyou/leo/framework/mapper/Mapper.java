package cn.acyou.leo.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义基础Mapper继承BaseMapper
 *
 * @author youfang
 * @version [1.0.0, 2022/5/10 11:01]
 */
public interface Mapper<T> extends BaseMapper<T> {
    /**
     * 插入数据，如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int insertIgnore(T entity);

    /**
     * 插入数据，当where sql 未查到的时候
     *
     * <pre>
     *  Dict dict = new Dict();
     *  dict.setCode("unit");
     *  dict.setParentId(0L);
     *  dict.setName("单位");
     *  dict.setStatus(0);
     *  int i = dictMapper.insertWhereNotExist(dict, "select name from sys_dict where id = 21560");
     * </pre>
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int insertWhereNotExist(@Param("o") T entity, @Param("whereSql") String whereSql);

    /**
     * 批量插入数据，如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entityList 实体类列表
     * @return 影响条数
     */
    int insertIgnoreBatch(List<T> entityList);

}
