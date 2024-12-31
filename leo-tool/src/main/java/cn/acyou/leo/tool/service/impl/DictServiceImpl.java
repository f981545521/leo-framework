package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.CollectionUtils;
import cn.acyou.leo.framework.util.TreeUtil;
import cn.acyou.leo.tool.dto.dict.DictSaveReq;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictTreeVo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.mapper.DictMapper;
import cn.acyou.leo.tool.service.DictService;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
@Slf4j
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

    @Override
    @Cached(name = "loadDictV", key = "#id", cacheType = CacheType.BOTH, expire = 100)
    public DictVo loadDict(long id) {
        log.info("使用数据库加载");
        DictVo dictVo = new DictVo();
        dictVo.setId(id);
        dictVo.setParentId(0L);
        dictVo.setName("load");
        return dictVo;
    }

    @Override
    @Cacheable(value = "TOOL:loadDictSpringV#200", key = "#id")
    public DictVo loadDictSpring(long id) {
        log.info("使用数据库加载");
        DictVo dictVo = new DictVo();
        dictVo.setId(id);
        dictVo.setParentId(0L);
        dictVo.setName("load");
        return dictVo;
    }

    /**
     * 受检异常（Checked Exceptions）
     * 定义：是在编译时期就被检查的异常。编译器会强制要求程序员处理这些异常，以保证程序在运行时能够更加健壮地应对可能出现的错误情况。
     * 处理要求：如果一个方法可能抛出受检异常，那么该方法要么使用try - catch块在方法内部捕获并处理这个异常，要么在方法的声明上使用throws关键字声明这个方法可能会抛出该异常，将异常处理的责任交给调用者。
     *
     * 非受检异常（Unchecked Exceptions）
     * 定义：也被称为运行时异常（Runtime Exceptions）。这些异常在编译时不会被检查，编译器不会强制要求程序员处理它们。它们通常是由于程序中的逻辑错误或者是一些不可预见但难以在编译时检查的情况导致的。
     * 处理要求：虽然编译器不强制要求处理，但在实际开发中，为了保证程序的稳定性和正确性，也应该尽可能地对这些异常进行处理。
     *
     * 所有受检异常都是Throwable类的直接或间接子类，并且不是RuntimeException类的子类。例如IOException、SQLException等都属于受检异常，它们都继承自Exception类（Exception类是Throwable的子类）。
     * 非受检异常是RuntimeException类及其子类。RuntimeException类本身也是Throwable的子类。例如ArithmeticException（算术异常）、NullPointerException（空指针异常）、ArrayIndexOutOfBoundsException（数组越界异常）等都是RuntimeException的子类，属于非受检异常。
     *
     * 默认是 RuntimeException 或者 Error 回滚
     * <pre>
     *  org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(java.lang.Throwable)
     *  @Override
     * 	public boolean rollbackOn(Throwable ex) {
     * 		return (ex instanceof RuntimeException || ex instanceof Error);
     * 	}
     *
     * </pre>
     *
     * @param dictSaveReq
     * @throws Exception
     */
    @Override
    @Transactional
    public void testExceptionSaveDict(DictSaveReq dictSaveReq) throws Exception {
        save(BeanCopyUtil.copy(dictSaveReq, Dict.class));
        if ("Unchecked".equals(dictSaveReq.getExThrow())) {
            int i = 1/0;
        }
        if ("Checked".equals(dictSaveReq.getExThrow())) {
            File file = new File("example.txt");
            FileInputStream fis = new FileInputStream(file);
        }
        log.info("保存成功");
    }
}
