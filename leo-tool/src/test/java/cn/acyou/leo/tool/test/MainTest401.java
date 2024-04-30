package cn.acyou.leo.tool.test;

import org.springframework.util.PatternMatchUtils;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/30 13:41]
 **/
public class MainTest401 {
    public static void main(String[] args) {
        System.out.println(PatternMatchUtils.simpleMatch("*.*.*","user.api.save"));
        System.out.println(PatternMatchUtils.simpleMatch("user.api.*","user.api.save"));
        System.out.println(PatternMatchUtils.simpleMatch("user.api1.*","user.api.save"));
    }
}
