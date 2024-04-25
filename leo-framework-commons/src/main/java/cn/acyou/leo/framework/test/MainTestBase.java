package cn.acyou.leo.framework.test;

import org.junit.jupiter.api.BeforeAll;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/25 11:13]
 **/
public class MainTestBase {

    @BeforeAll
    public static void loadProperties() {
        try {
            TestUtils.loadExtendProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
