package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.tool.entity.Order1;
import cn.acyou.leo.tool.mapper.Order1Mapper;
import cn.acyou.leo.tool.service.Order1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author youfang
 * @since 2025-08-28
 */
@Service
@RequiredArgsConstructor
public class Order1ServiceImpl implements Order1Service{
    private final Order1Mapper order1Mapper;

}
