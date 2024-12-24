package cn.acyou.leo.tool.mapper;

import cn.acyou.leo.framework.mapper.Mapper;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface DictMapper extends Mapper<Dict> {

    /**
     * 根据code查询所有数据字典值
     *
     * @param code 代码
     * @return {@link List <DictVo>}
     */
    List<DictVo> listDictByCode(String code);

    /**
     * 分页查询数据字典
     *
     * @param dictSo 查询条件
     * @return {@link List<DictVo>}
     */
    List<DictVo> selectDicts(DictSo dictSo);

    /**
     * 查询字典
     *
     * @param dictCode 字典代码
     * @param value    字典值
     * @return {@link Dict}
     */
    Dict selectDict(@Param("dictCode") String dictCode, @Param("value") String value);


    @Update("UPDATE sys_dict SET remark = #{remark} WHERE id = #{id}")
    int updateRemark(Dict user);

    /**
     * 在查询不到的时候新增
     * @param dictList list
     */
    void insertListNotExists(List<Dict> dictList);
}
