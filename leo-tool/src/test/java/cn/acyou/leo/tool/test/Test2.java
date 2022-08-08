package cn.acyou.leo.tool.test;

import cn.hutool.core.thread.ConcurrencyTester;
import org.junit.jupiter.api.Test;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/5 16:57]
 **/
public class Test2 {
    @Test
    public void test1() {
        ConcurrencyTester ct = new ConcurrencyTester(10);
        ct.test(() -> {
            System.out.println("ok");
        });
    }
}
