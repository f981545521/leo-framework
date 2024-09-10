package cn.acyou.leo.tool.feign;

import com.github.lianjiatech.retrofit.spring.boot.degrade.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2024/8/29 14:36]
 **/
@Slf4j
@Component
public class RemoteFallbackFactory implements FallbackFactory<HttpbinClient> {

    @Override
    public HttpbinClient create(Throwable cause) {
        log.info("Fallback: 服务调用失败; 原因: {}", cause.getMessage());
        return new HttpbinClient() {
            @Override
            public String ip() {
                return "服务调用失败！";
            }

            @Override
            public String ip2() {
                return "服务调用失败！";
            }
        };
    }
}
