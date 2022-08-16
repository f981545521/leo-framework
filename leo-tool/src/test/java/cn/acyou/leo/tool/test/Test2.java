package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.DateUtil;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/5 16:57]
 **/
public class Test2 {
    @Test
    public void test1() {
        ConcurrencyTester ct = new ConcurrencyTester(5);
        ct.test(() -> {
            String post = HttpUtil.post("https://172.16.17.21:8443/operate/userGradeMapping/getUserGradeByUserId", "{\"userId\":1561}");
            System.out.println(post);
        });
    }

    @Test
    public void test2() {
        List<String> strings = Arrays.asList("111", "222", "333");
        strings.add("ok");
        System.out.println(strings);
    }

    @Test
    public void test3() {
        //01天19小时11分钟
        Date add = DateUtil.add(new Date(), 0, 0, 1, 19, 11, 0);
        System.out.println(DateUtil.getDateFormat(add));
        //01天06小时37分钟
        Date add2 = DateUtil.add(new Date(), 0, 0, 1, 6, 37, 0);
        System.out.println(DateUtil.getDateFormat(add2));
    }
}
