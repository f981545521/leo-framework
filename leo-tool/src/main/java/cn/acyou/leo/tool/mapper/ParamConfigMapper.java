package cn.acyou.leo.tool.mapper;

import cn.acyou.leo.tool.entity.ParamConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 参数配置表 Mapper 接口
 * </p>
 *
 * @author youfang
 * @since 2022-03-26
 */
public interface ParamConfigMapper extends BaseMapper<ParamConfig> {

    List<ParamConfig> selectBySql(@Param("sql") String sql);
}
