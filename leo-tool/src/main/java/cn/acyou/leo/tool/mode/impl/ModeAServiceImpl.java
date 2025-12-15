package cn.acyou.leo.tool.mode.impl;


import cn.acyou.leo.tool.mode.ModeService;
import cn.acyou.leo.tool.mode.VersionedService;
import lombok.RequiredArgsConstructor;

/**
 * @author youfang
 * @version [1.0.0, 2025/12/15 11:01]
 **/
@VersionedService(value = VersionedService.VersionType.VERSION_A)
@RequiredArgsConstructor
public class ModeAServiceImpl implements ModeService {

    @Override
    public String getName() {
        return "my name is ModeA";
    }
}
