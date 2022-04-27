package cn.acyou.leo.tool.service;

import cn.acyou.leo.tool.dto.area.AreaVo;
import cn.acyou.leo.tool.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地区信息表 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface AreaService extends IService<Area> {

    /**
     * 根据ID查询下一个区域
     *
     * @param id id
     * @return {@link List <AreaVo>}
     */
    List<AreaVo> getNextArea(String id);
}
