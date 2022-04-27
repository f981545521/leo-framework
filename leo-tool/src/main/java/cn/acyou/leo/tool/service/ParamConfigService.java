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

    Map<String, ParamConfigVo> getConfigMap(String namespace, String code);

    List<ParamConfigVo> getConfigList(String namespace, String code);

    void clearAllCache();

    PageData<ParamConfigVo> pageSelect(ParamConfigSo paramConfigSo);
}
