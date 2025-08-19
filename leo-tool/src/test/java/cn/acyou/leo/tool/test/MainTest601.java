package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2025/6/12 13:47]
 **/
public class MainTest601 {

    @Test
    public void test1(){
        Long memberId = 66600142586655L;
        System.out.println(StringUtils.substring(memberId + "", 5));
        System.out.println(StringUtils.removeStart(memberId + "", "66600"));
        System.out.println(StringUtils.removeStart(memberId + "", "1000"));
        System.out.println(StringUtils.substringAfter(memberId + "", 5));
    }


    public static void main(String[] args) {
        System.out.println(new Date().toString());
        System.out.println(DateUtil.parse("2024-12-21"));
        System.out.println(DateUtil.parse("2024-12-21 12:00:00"));
        System.out.println(DateUtil.parse("2024年12月21日 12时00分00秒"));
        System.out.println(DateUtil.parse("2024/12/21 12:00:00"));
        System.out.println(DateUtil.parse("20241221 12:00:00"));
        System.out.println(DateUtil.parse("2025-06-12T08:00:00.000Z"));
        System.out.println(DateUtil.parse("20250612T080000Z"));
        System.out.println(cn.hutool.core.date.DateUtil.parseDateTime("2024-12-21"));
    }
}
