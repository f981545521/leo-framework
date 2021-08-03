package cn.acyou.leo.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
@Component
public class SpringHelper implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * 获取 Spring Bean工厂
     * @return beanFactory
     */
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringHelper.beanFactory = beanFactory;
    }

    /**
     * 获取指定name的bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取指定type的Bean
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return beanFactory.getBean(clz);
    }

    /**
     * 包含Bean
     *
     * @param name beanClassName
     * @return true/false
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name beanName
     * @return boolean
     */
    public static boolean isSingleton(String name) {
        return beanFactory.isSingleton(name);
    }
}
