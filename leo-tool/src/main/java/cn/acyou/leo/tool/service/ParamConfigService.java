package cn.acyou.leo.tool.service;

import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.tool.dto.param.ParamConfigSo;
import cn.acyou.leo.tool.dto.param.ParamConfigVo;
import cn.acyou.leo.tool.entity.ParamConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author youfang
 * @since 2022-03-26
 */
public interface ParamConfigService extends IService<ParamConfig> {

    /**
     * 得到配置图
     *
     * @param namespace 名称空间
     * @param code      代码
     * @return {@link Map}<{@link String}, {@link ParamConfigVo}>
     */
    Map<String, ParamConfigVo> getConfigMap(String namespace, String code);

    /**
     * 得到配置列表
     *
     * @param namespace 名称空间
     * @param code      代码
     * @return {@link List}<{@link ParamConfigVo}>
     */
    List<ParamConfigVo> getConfigList(String namespace, String code);

    /**
     * 获取配置
     *
     * @param namespace 名称空间
     * @param code      代码
     * @return {@link ParamConfigVo}
     */
    ParamConfigVo getConfig(String namespace, String code);

    /**
     * 清除所有缓存
     */
    void clearAllCache();

    /**
     * 分页查询
     *
     * @param paramConfigSo 参数配置查询参数
     * @return {@link PageData}<{@link ParamConfigVo}>
     */
    PageData<ParamConfigVo> pageSelect(ParamConfigSo paramConfigSo);

    /**
     * 获取值 没有配置或者配置不正确时返回默认值
     *
     * @param namespace    名称空间
     * @param code         code
     * @param defaultValue 默认值
     */
    String getValueOrDefault(String namespace, String code, String defaultValue);
}
