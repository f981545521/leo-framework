package cn.acyou.leo.order.web.base;

import cn.acyou.leo.product.web.LeoProductWebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-06 17:42]
 */
@SpringBootTest(classes = LeoProductWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationBaseTests {

    @Test
    public void testStart(){
        System.out.println("ok");
    }

}
