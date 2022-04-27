package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.CollectionUtils;
import cn.acyou.leo.framework.util.TreeUtil;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictTreeVo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.mapper.DictMapper;
import cn.acyou.leo.tool.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    /**
     * 查询所有顶级数据字典
     *
     * @return {@link List < DictVo >}
     */
    @Override
    public List<DictVo> selectAllParentType() {
        List<Dict> parentDict = lambdaQuery()
                .eq(Dict::getParentId, "0")
                .list();
        return BeanCopyUtil.copyList(parentDict, DictVo.class);
    }

    /**
     * 根据code查询所有数据字典值
     *
     * @param code 代码
     * @return {@link List<DictVo>}
     */
    @Override
    public List<DictVo> listDictByCode(String code) {
        Assert.notNull(code, "查询Code不能为空！");
        Dict parentDict = lambdaQuery().eq(Dict::getCode, code).eq(Dict::getParentId, "0").one();
        if (parentDict == null) {
            throw new ServiceException("数据字典类型不存在，请检查");
        }
        if (parentDict.getStatus().equals(Constant.FLAG_FALSE_0)) {
            throw new ServiceException("数据字典{}被禁用！", code);
        }
        return baseMapper.listDictByCode(code);
    }

    /**
     * 分页查询数据字典
     *
     * @param dictSo 分页信息
     * @return {@link PageData <DictVo>}
     */
    @Override
    public PageData<DictVo> pageSelectDicts(DictSo dictSo) {
        return PageQuery.startPage(dictSo).selectMapper(baseMapper.selectDicts(dictSo));
    }

    /**
     * 分页查询数据字典(树形结构)
     *
     * @param dictSo 分页信息
     * @return {@link PageData<DictVo>}
     */
    @Override
    public PageData<DictTreeVo> treeSelectDicts(DictSo dictSo) {
        PageData<DictTreeVo> dictTreeVoPageData = PageQuery.startPage(dictSo).selectMapper(baseMapper.selectDicts(dictSo), DictTreeVo.class);
        List<DictTreeVo> dataList = dictTreeVoPageData.getList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<Long> collect = dataList.stream().map(DictVo::getId).collect(Collectors.toList());
            List<Dict> childValues = lambdaQuery().in(Dict::getParentId, collect).list();
            if (CollectionUtils.isNotEmpty(childValues)) {
                dataList.addAll(BeanCopyUtil.copyList(childValues, DictTreeVo.class));
            }
            List<DictTreeVo> dictTreeVos = TreeUtil.buildTrees(dataList);
            dictTreeVoPageData.setList(dictTreeVos);
        }
        return dictTreeVoPageData;
    }

    /**
     * 有效的dict类型值
     *
     * @param dictCode dict类型代码
     * @param value    价值
     * @return boolean
     */
    public boolean validDictValue(String dictCode, String value) {
        Dict dict = baseMapper.selectDict(dictCode, value);
        return dict != null;
    }
}
