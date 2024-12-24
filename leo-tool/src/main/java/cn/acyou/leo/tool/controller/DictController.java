package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.IdReq;
import cn.acyou.leo.framework.model.IdsReq;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.Assert;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.tool.dto.dict.DictSaveReq;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictTreeVo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典表 Controller
 * 2020-07-16 14:55:51 数据字典表 接口
 *
 * @author youfang
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@Api(value = "数据字典表", tags = "数据字典接口")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation(value = "查询所有顶级数据字典")
    @PostMapping("/allType")
    public Result<?> allType() {
        List<DictVo> dictVoList = dictService.selectAllParentType();
        return Result.success(dictVoList);
    }

    @ApiOperation(value = "根据code查询所有数据字典值")
    @PostMapping("/list")
    public Result<?> listDictByCode(@RequestParam String code) {
        List<DictVo> dictVoList = dictService.listDictByCode(code);
        return Result.success(dictVoList);
    }

    @ApiOperation(value = "分页查询所有数据字典", nickname = Constant.ALL)
    @PostMapping("/page")
    public Result<?> page(@RequestBody DictSo dictSo) {
        PageData<DictVo> pageData = dictService.pageSelectDicts(dictSo);
        return Result.success(pageData);
    }

    @ApiOperation(value = "分页查询所有数据字典(树形结构)")
    @PostMapping("/tree")
    public Result<?> tree(@RequestBody DictSo dictSo) {
        PageData<DictTreeVo> pageData = dictService.treeSelectDicts(dictSo);
        return Result.success(pageData);
    }

    @ApiOperation(value = "根据数据字典ID查询单条记录")
    @PostMapping("/get")
    public Result<?> get(Long id) {
        Dict dict = dictService.getById(id);
        return Result.success(BeanCopyUtil.copy(dict, DictVo.class));
    }

    @ApiOperation(value = "保存数据字典")
    @PostMapping("/saveOrUpdate")
    public Result<?> saveOrUpdate(@Validated @RequestBody DictSaveReq dictSaveReq) {
        if (dictSaveReq.getId() != null) {
            Assert.notNull(dictSaveReq.getId(), "修改数据字典，主键不能为空");
            Assert.ifTrue(dictSaveReq.getId().equals(dictSaveReq.getParentId()), "上级不能是自己！");
            dictService.updateById(BeanCopyUtil.copy(dictSaveReq, Dict.class));
        } else {
            dictService.save(BeanCopyUtil.copy(dictSaveReq, Dict.class));
        }
        return Result.success();
    }

    @ApiOperation(value = "修改数据字典")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody DictSaveReq dictSaveReq) {
        Assert.notNull(dictSaveReq.getId(), "修改数据字典，主键不能为空");
        Assert.ifTrue(dictSaveReq.getId().equals(dictSaveReq.getParentId()), "上级不能是自己！");
        dictService.updateById(BeanCopyUtil.copy(dictSaveReq, Dict.class));
        return Result.success();
    }

    @ApiOperation(value = "删除数据字典")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody IdsReq idsReq) {
        dictService.removeByIds(idsReq.getIds());
        return Result.success();
    }


    @ApiOperation(value = "JetCache使用")
    @PostMapping("/loadDict")
    public Result<DictVo> loadDict(@Validated @RequestBody IdReq idReq) {
        DictVo dictVo = dictService.loadDict(idReq.getId());
        return Result.success(dictVo);
    }

    @ApiOperation(value = "SpringCache使用")
    @PostMapping("/loadDictSpring")
    public Result<DictVo> loadDictSpring(@Validated @RequestBody IdReq idReq) {
        DictVo dictVo = dictService.loadDictSpring(idReq.getId());
        return Result.success(dictVo);
    }

}
