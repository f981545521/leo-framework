package cn.acyou.leo.tool.mapper;

import cn.acyou.leo.tool.entity.ScheduleJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 定时任务 Mapper 接口
 * </p>
 *
 * @author youfang
 * @since 2022-04-27
 */
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

    ScheduleJob selectByJobId(Long jobId);

    ScheduleJob selectByJobIdV2(Long jobId);

    ScheduleJob selectByJobIdV3(Long jobId);

    List<ScheduleJob> selectLimit10();
}
