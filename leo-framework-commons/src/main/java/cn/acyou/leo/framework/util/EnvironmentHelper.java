package cn.acyou.leo.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/9 11:21]
 **/
@Component
public class EnvironmentHelper {
    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        EnvironmentHelper.environment = environment;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static String getRequiredProperty(String key) {
        return environment.getRequiredProperty(key);
    }

    public static String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }

    public static String[] getDefaultProfiles() {
        return environment.getDefaultProfiles();
    }

    public static String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    public static String resolveRequiredPlaceholders(String text) {
        return environment.resolveRequiredPlaceholders(text);
    }

    public static String getPropertySystem(String key) {
        return System.getProperty(key);
    }

    public static String getPropertySystem(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    public static Properties getPropertiesSystem() {
        return System.getProperties();
    }

    public static Map<String, String> getenvSystem() {
        return System.getenv();
    }

}
