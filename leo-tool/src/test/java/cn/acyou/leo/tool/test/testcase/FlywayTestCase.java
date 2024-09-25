package cn.acyou.leo.tool.test.testcase;

import cn.acyou.leo.tool.test.ApplicationBaseTests;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.output.ValidateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/23 9:43]
 **/
@Slf4j
public class FlywayTestCase extends ApplicationBaseTests {
    @Autowired
    private Flyway flyway;

    @Test
    public void test1(){
        MigrationInfoService info = flyway.info();
        System.out.println(info);
        ValidateResult validateResult = flyway.validateWithResult();
        System.out.println(validateResult);
        flyway.migrate();
        System.out.println("end");
    }


}
