package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.tool.dto.area.AreaVo;
import cn.acyou.leo.tool.entity.Area;
import cn.acyou.leo.tool.mapper.AreaMapper;
import cn.acyou.leo.tool.service.AreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地区信息表 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    /**
     * 根据ID查询下一个区域
     *
     * @param id id
     * @return {@link List < AreaVo >}
     */
    @Override
    @Cacheable(value = "TOOL:AREA", key = "#id")
    public List<AreaVo> getNextArea(String id) {
        List<Area> areas = lambdaQuery().eq(Area::getParentId, id).list();
        return BeanCopyUtil.copyList(areas, AreaVo.class);
    }
}
