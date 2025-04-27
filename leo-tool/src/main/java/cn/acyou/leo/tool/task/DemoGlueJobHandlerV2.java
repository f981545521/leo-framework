/*
package cn.acyou.leo.tool.task;

import cn.acyou.leo.tool.service.common.CommonService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoGlueJobHandlerV2 extends IJobHandler {
    @Autowired
    private CommonService commonService;

    @Override
    public void execute() throws Exception {
        XxlJobHelper.log("XXL-JOB, run start...");
        String result = commonService.sayHello(XxlJobHelper.getJobParam());
        XxlJobHelper.handleResult(200, result);
        XxlJobHelper.log("XXL-JOB, run end result:" + result);
    }
}
*/
