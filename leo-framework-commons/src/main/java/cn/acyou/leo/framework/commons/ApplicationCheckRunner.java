package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.prop.LeoProperty;
import cn.acyou.leo.framework.service.UserTokenService;
import cn.acyou.leo.framework.util.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2022-3-16]
 **/
@Component
public class ApplicationCheckRunner implements ApplicationRunner {
    @Autowired
    private LeoProperty leoProperty;

    @Override
    public void run(ApplicationArguments args) {
        if (leoProperty.isTokenVerify()) {
            //启动Token校验时，必须存在UserTokenService
            SpringHelper.getBean(UserTokenService.class);
        }
    }
}
