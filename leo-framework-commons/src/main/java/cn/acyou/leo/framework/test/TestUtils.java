package cn.acyou.leo.framework.test;

import cn.acyou.leo.framework.exception.AssertException;
import cn.acyou.leo.framework.util.StringUtils;
import org.springframework.core.io.FileSystemResource;

import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/25 11:09]
 **/
public class TestUtils {

    public static String DEFAULT_PATH = "D:\\workspace\\work\\private_account.properties";

    public static void loadExtendProperties() {
        loadExtendProperties(DEFAULT_PATH);
    }

    public static void loadExtendProperties(String path) {
        try {
            Properties properties = System.getProperties();
            properties.load(new FileSystemResource(path).getInputStream());
            System.setProperties(properties);
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
