package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.prop.LeoProperty;
import cn.acyou.leo.framework.service.UserTokenService;
import cn.acyou.leo.framework.util.IPUtil;
import cn.acyou.leo.framework.util.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2022-3-16]
 **/
@Slf4j
@Component
public class ApplicationCheckRunner implements ApplicationRunner {
    @Autowired
    private LeoProperty leoProperty;
    @Autowired(required = false)
    private LoggingSystem loggingSystem;
    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        if (leoProperty.isTokenVerify()) {
            //启动Token校验时，必须存在UserTokenService
            SpringHelper.getBean(UserTokenService.class);
        }
        //去除频繁的无关紧要的日志
        //loggingSystem.setLogLevel("cn.acyou.leo.pay.StudentMapper.selectList", LogLevel.WARN);
        //打印接口地址，方便访问
        try {
            SpringHelper.getBean("defaultApi2");
            String port = environment.getProperty("server.port", "8080");
            String contextPath = environment.getProperty("server.servlet.context-path", "");
            String addr = String.format("http://%s:%s%s/doc.html", IPUtil.getLocalIP(), port, contextPath);
            log.info("[接口文档已经启用]({})", addr);
        } catch (Exception e) {
            //ignore
        }
        String jvmName = environment.getProperty("java.vm.name");
        String jvmVersion = environment.getProperty("java.version");
        String osName = environment.getProperty("os.name");
        String activeProfile = environment.getActiveProfiles()[0];
        log.info("当前运行环境:[{}] ### 当前操作系统：[{}] {}[{}]", activeProfile, osName, jvmName, jvmVersion);
    }
}
