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
     * 打印请求参数
     */
    private boolean printRequestParam = true;

    /**
     * 接口统计分析
     */
    private boolean interfaceCallStatistics = true;

    /**
     * 接口统计分析 忽略的接口地址
     */
    private List<String> ignoreUriList = new ArrayList<>();
}