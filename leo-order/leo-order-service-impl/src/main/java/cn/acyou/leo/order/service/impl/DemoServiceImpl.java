package cn.acyou.leo.order.service.impl;

import cn.acyou.leo.order.client.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-03 14:12]
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String sayHello(String name) {
        return String.format("[%s] : Hello, %s", serviceName, name);
    }
}
