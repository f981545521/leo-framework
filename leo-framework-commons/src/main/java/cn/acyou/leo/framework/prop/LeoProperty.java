package cn.acyou.leo.framework.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-17]
 **/
@Data
@ConfigurationProperties(prefix = "leo.debug")
public class LeoProperty {

    /**
     * 打印异常到Result中
     */
    private boolean printToResult = true;

    /**
     * 打印请求体
     */
    private boolean printRequestBody = true;

    /**
     * 打印响应信息
     */
    private boolean printResponseBody = true;

    /**
     * SpringMvcInterceptor Token校验
     */
    private boolean tokenVerify = false;

    /**
     * 接口统计分析
     */
    private boolean interfaceCallStatistics = false;

    /**
     * 接口统计分析 忽略的接口地址
     */
    private List<String> ignoreUriList = new ArrayList<>();
}