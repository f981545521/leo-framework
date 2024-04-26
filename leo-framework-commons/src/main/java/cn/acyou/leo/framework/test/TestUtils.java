package cn.acyou.leo.framework.test;

import cn.acyou.leo.framework.exception.AssertException;
import cn.acyou.leo.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/25 11:09]
 **/
@Slf4j
public class TestUtils {

    public static String DEFAULT_PATH = "D:\\workspace\\work\\private_account.properties";
    public static String DEFAULT_PATH_2 = "E:\\workspace\\work\\private_account.properties";

    public static void loadExtendProperties() {
        loadExtendProperties(DEFAULT_PATH);
        loadExtendProperties(DEFAULT_PATH_2);
    }

    public static void loadExtendProperties(String path) {
        try {
            if (new File(path).exists()) {
                Properties properties = System.getProperties();
                properties.load(new FileSystemResource(path).getInputStream());
                System.setProperties(properties);
                log.info("加载配置成功 {}", path);
            }else {
                log.info("加载配置失败 {}", path);
            }
        }catch (Exception e) {
            //ignore
        }

    }


    public static String getProperty(String key) {
        return System.getProperty(key);
    }

    public static String getPropertyRequired(String key) {
        String property = getProperty(key);
        if (StringUtils.isBlank(property)) {
            throw new AssertException("配置[" + key + "]为空，请检查！");
        }
        return property;
    }
}
