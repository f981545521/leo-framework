package cn.acyou.leo.pay.service.impl;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.pay.dto.ParamConfigVo;
import cn.acyou.leo.pay.entity.ParamConfig;
import cn.acyou.leo.pay.mapper.ParamConfigMapper;
import cn.acyou.leo.pay.service.ParamConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    @Cacheable(value="leo:pay:paramConfig#-1", key="#namespace + '-' + #code")
    public List<ParamConfigVo> getConfigList(String namespace, String code) {
        List<ParamConfig> list = lambdaQuery()
                .in(StringUtils.isNotBlank(namespace), ParamConfig::getNamespace, (Object[]) namespace.split(","))
                .eq(StringUtils.isNotBlank(code), ParamConfig::getCode, code)
                .eq(ParamConfig::getIsDelete, Constant.FLAG_FALSE_0)
                .orderByDesc(ParamConfig::getCreateTime)
                .list();
        return BeanCopyUtil.copyList(list, ParamConfigVo.class);
    }

    @Override
    @CacheEvict(value = "leo:pay:paramConfig", allEntries = true)
    public void clearAllCache() {
        log.info("清除所有缓存成功！");
    }
}
