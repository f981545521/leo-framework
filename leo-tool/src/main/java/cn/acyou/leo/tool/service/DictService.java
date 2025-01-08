package cn.acyou.leo.tool.service;

import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.tool.dto.dict.DictSaveReq;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictTreeVo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface DictService extends IService<Dict> {

    /**
     * 查询所有顶级数据字典
     *
     * @return {@link List <DictVo>}
     */
    List<DictVo> selectAllParentType();

    /**
     * 根据code查询所有数据字典值
     *
     * @param code 代码
     * @return {@link List<DictVo>}
     */
    List<DictVo> listDictByCode(String code);

    /**
     * 分页查询数据字典
     *
     * @param dictSo 分页信息
     * @return {@link PageData<DictVo>}
     */
    PageData<DictVo> pageSelectDicts(DictSo dictSo);

    /**
     * 分页查询数据字典(树形结构)
     *
     * @param dictSo 分页信息
     * @return {@link PageData<DictVo>}
     */
    PageData<DictTreeVo> treeSelectDicts(DictSo dictSo);

    /**
     * 测试 JetCache多级缓存
     * @param id id
     * @return {@link DictVo }
     */
    DictVo loadDict(long id);
    /**
     * 测试 SpringCache缓存
     * @param id id
     * @return {@link DictVo }
     */
    DictVo loadDictSpring(long id);

    void testExceptionSaveDict(DictSaveReq saveReq) throws Exception;

    void testSaveReadOnly(DictSaveReq saveReq);
}
