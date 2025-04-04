package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.tool.dto.param.ParamConfigSo;
import cn.acyou.leo.tool.dto.param.ParamConfigVo;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.mapper.ParamConfigMapper;
import cn.acyou.leo.tool.service.ParamConfigService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author youfang
 * @since 2022-03-26
 */
@Slf4j
@Service
public class ParamConfigServiceImpl extends ServiceImpl<ParamConfigMapper, ParamConfig> implements ParamConfigService {

    @Autowired
    private ParamConfigService baseService;

    @Override
    public Map<String, ParamConfigVo> getConfigMap(String namespace, String code) {
        Map<String, ParamConfigVo> configVoMap = new LinkedHashMap<>();
        List<ParamConfigVo> configList = baseService.getConfigList(namespace, code);
        for (ParamConfigVo configVo : configList) {
            configVoMap.put(configVo.getCode(), configVo);
        }
        return configVoMap;
    }

    @Override
    //@Cacheable(value="leo:pay:paramConfig#-1", key="#namespace + '-' + #code")
    public List<ParamConfigVo> getConfigList(String namespace, String code) {
        LambdaQueryChainWrapper<ParamConfig> queryChainWrapper = lambdaQuery()
                .in(StringUtils.isNotBlank(namespace), ParamConfig::getNamespace, (Object[]) namespace.split(","))
                //.in(StringUtils.isNotEmpty(code), ParamConfig::getCode, (Object[]) code.split(","))
                .eq(ParamConfig::getDeleted, Constant.FLAG_FALSE_0)
                .orderByDesc(ParamConfig::getSort);
        if (StringUtils.isNotBlank(code)) {
            queryChainWrapper.in(StringUtils.isNotEmpty(code), ParamConfig::getCode, (Object[]) code.split(","));
        }
        List<ParamConfig> list = queryChainWrapper.list();
        return BeanCopyUtil.copyList(list, ParamConfigVo.class);
    }

    @Override
    public ParamConfigVo getConfig(String namespace, String code) {
        List<ParamConfigVo> configList = baseService.getConfigList(namespace, code);
        if (!CollectionUtils.isEmpty(configList)) {
            return BeanCopyUtil.copy(configList.get(0), ParamConfigVo.class);
        }
        return null;
    }

    @Override
    public PageData<ParamConfig> pageSelect(ParamConfigSo paramConfigSo) {
        return PageQuery.startPage(paramConfigSo).selectMapper(lambdaQuery()
                .eq(StringUtils.isNotBlank(paramConfigSo.getNamespace()), ParamConfig::getNamespace, paramConfigSo.getNamespace())
                .eq(StringUtils.isNotBlank(paramConfigSo.getCode()), ParamConfig::getCode, paramConfigSo.getCode())
                .orderByDesc(ParamConfig::getSort)
                .list());
    }

    @Override
    public String getValueOrDefault(String namespace, String code, String defaultValue) {
        ParamConfigVo baseConfig = getConfig(namespace, code);
        if (baseConfig != null) {
            String configValue = baseConfig.getValue();
            if (StringUtils.isNotBlank(configValue)) {
                return configValue;
            }
        }
        return defaultValue;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        boolean update = lambdaUpdate()
                .set(ParamConfig::getStatus, status)
                .eq(ParamConfig::getId, id)
                .ne(ParamConfig::getStatus, status)
                .update();
        WorkUtil.trySleep5000();
        if (update) {
            log.info("操作成功！ id :{} status:{}", id, status);
        } else {
            throw new ServiceException("已经完成操作。");
        }
    }

    @Override
    @CacheEvict(value = "leo:pay:paramConfig", allEntries = true)
    public void clearAllCache() {
        log.info("清除所有缓存成功！");
    }

    @Override
    public List<ParamConfig> selectBySql(String sql) {
        return baseMapper.selectBySql(sql);
    }
}
