package cn.acyou.leo.tool.mapper;

import cn.acyou.leo.tool.entity.Area;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 地区信息表 Mapper 接口
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface AreaMapper extends BaseMapper<Area> {

    /**
     * 递归查询 包括父级分类
     *
     * @param id 类别id
     * @return {@link List < Area >}
     */
    List<Area> selectIncludeParent(Long id);

    /**
     * 递归查询 包括子级分类
     *
     * @param id 类别id
     * @return {@link List< Area >}
     */
    List<Area> selectIncludeChild(Long id);
}
