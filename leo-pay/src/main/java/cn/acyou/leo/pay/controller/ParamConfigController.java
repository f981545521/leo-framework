package cn.acyou.leo.pay.controller;


import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.pay.dto.ParamConfigVo;
import cn.acyou.leo.pay.service.ParamConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "参数配置")
public class ParamConfigController {

    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation("获取配置(Map结构)")
    @GetMapping("map")
    public Result<Map<String, ParamConfigVo>> getConfigMap(@RequestParam("namespace") String namespace, String code){
        Map<String, ParamConfigVo> configMap =  paramConfigService.getConfigMap(namespace, code);
        return Result.success(configMap);
    }

    @ApiOperation("获取配置(List结构)")
    @GetMapping("list")
    public Result<List<ParamConfigVo>> getConfigList(@RequestParam("namespace") String namespace, String code){
        List<ParamConfigVo> configList =  paramConfigService.getConfigList(namespace, code);
        return Result.success(configList);
    }

    @ApiOperation("清除所有缓存")
    @GetMapping("clearAllCache")
    public Result<Void> clearAllCache(){
        paramConfigService.clearAllCache();
        return Result.success();
    }


}
