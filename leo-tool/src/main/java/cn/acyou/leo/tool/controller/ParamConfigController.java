package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.annotation.authz.RequiresRoles;
import cn.acyou.leo.framework.model.IdsReq;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.tool.dto.param.ParamConfigSo;
import cn.acyou.leo.tool.dto.param.ParamConfigVo;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.service.ParamConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author youfang
 * @since 2022-03-26
 */
@RestController
@RequestMapping("/paramConfig")
@Api(tags = "参数配置接口")
public class ParamConfigController {

    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation("获取配置(单个)")
    @GetMapping("get")
    public Result<ParamConfigVo> getConfigMap(@RequestParam("namespace") String namespace, String code) {
        ParamConfigVo config = paramConfigService.getConfig(namespace, code);
        return Result.success(config);
    }

    @ApiOperation("获取配置(Map结构)")
    @GetMapping("map")
    public Result<Map<String, ParamConfigVo>> mapConfigMap(@RequestParam("namespace") String namespace, String code) {
        Map<String, ParamConfigVo> configMap = paramConfigService.getConfigMap(namespace, code);
        return Result.success(configMap);
    }

    @ApiOperation("获取配置(List结构)")
    @GetMapping("list")
    public Result<List<ParamConfigVo>> getConfigList(@RequestParam("namespace") String namespace, String code) {
        List<ParamConfigVo> configList = paramConfigService.getConfigList(namespace, code);
        return Result.success(configList);
    }

    @ApiOperation("分页查询所有配置")
    @PostMapping("page")
    public Result<PageData<ParamConfig>> page(@RequestBody ParamConfigSo paramConfigSo) {
        PageData<ParamConfig> configList = paramConfigService.pageSelect(paramConfigSo);
        return Result.success(configList);
    }

    @ApiOperation("清除所有缓存")
    @GetMapping("clearAllCache")
    @RequiresRoles("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> clearAllCache() {
        paramConfigService.clearAllCache();
        return Result.success();
    }

    @ApiOperation("修改状态")
    @PostMapping("status")
    @RequiresRoles("ADMIN")
    public Result<Void> status(@RequestParam Long id, @RequestParam Integer status) {
        paramConfigService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody IdsReq ids) {
        paramConfigService.removeByIds(ids.getIds());
        return Result.success();
    }

    @ApiOperation("新增/修改")
    @PostMapping("saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody ParamConfig paramConfig) {
        paramConfigService.saveOrUpdate(paramConfig);
        return Result.success();
    }


}
