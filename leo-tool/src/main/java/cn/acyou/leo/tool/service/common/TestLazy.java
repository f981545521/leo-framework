package cn.acyou.leo.tool.service.common;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2025/2/10 19:49]
 **/
@Component
//@Lazy(false) //懒加载时静态属性无法加载、需要false
public class TestLazy {
    private static String name;

    @Value("${spring.main.lazy-initialization}")
    public void setBucketName(String bucketName) {
        TestLazy.name = name;
    }
}
