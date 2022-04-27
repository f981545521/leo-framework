package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.dto.area.AreaVo;
import cn.acyou.leo.tool.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 地区信息表 前端控制器
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@RestController
@RequestMapping("/area")
@Api(value = "地区表", tags = "地区接口")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "查询下级地区")
    @PostMapping("/nextArea")
    public Result<List<AreaVo>> nextArea(String id) {
        if (StringUtils.isBlank(id)) {
            id = "100000";
        }
        List<AreaVo> areaVoList = areaService.getNextArea(id);
        return Result.success(areaVoList);
    }
}
