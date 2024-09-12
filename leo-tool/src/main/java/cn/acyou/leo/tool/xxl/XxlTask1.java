package cn.acyou.leo.tool.xxl;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/12 16:43]
 **/
@Slf4j
@Component
public class XxlTask1 {

    @XxlJob("oneHandler")
    public void oneHandler() throws Exception {
        log.info("执行任务成功");
        XxlJobHelper.log("XXL-JOB, 执行任务成功.");
    }
}
