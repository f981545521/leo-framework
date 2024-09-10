package cn.acyou.leo.tool.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author youfang
 * @version [1.0.0, 2024/8/29 11:53]
 **/
@FeignClient(name = "httpbin", //如果没有配置url那么配置的值将作为服务名称，用于服务发现。
        url = "https://httpbin.org",//优先级比服务名（name）高
        contextId = "httpbin",//如果是默认属性的话，那么一个客户端只能有一个@FeignClient接口 注：contextId不能带_等符号
        // decode404 = true,
        fallback = RemoteFallbackFactory.class)
public interface HttpbinClient {

    @GetMapping("/ip")
    String ip();

    @GetMapping("/ip2")
    String ip2();

}
