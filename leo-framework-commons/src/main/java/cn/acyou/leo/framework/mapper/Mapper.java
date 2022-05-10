package cn.acyou.leo.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
     * 批量插入数据，如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entityList 实体类列表
     * @return 影响条数
     */
    int insertIgnoreBatch(List<T> entityList);

}
