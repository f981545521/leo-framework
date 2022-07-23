package cn.acyou.leo.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Spring 工具
 *
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
     * 获取指定type的Bean列表
     */
    public static <T> Collection<T> getBeans(Class<T> clz) throws BeansException {
        final Map<String, T> beanMap = beanFactory.getBeansOfType(clz);
        return beanMap.values();
    }

    /**
     * 是否是应用态运行的
     */
    public static boolean isApplicationRunning() throws BeansException {
        return beanFactory != null;
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

    /**
     * 创建指定type的Bean，执行bean的完整初始化，包括所有适用的BeanPostProcessor。
     * <ul>
     *     <li>1. 可以进行AutoWired注解的注入</li>
     *     <li>2. 可以进行Constructor的注入</li>
     * </ul>
     * <p>注意： 此处创建的bean为原型，不受Spring管理。<strong>可以被GC回收！!<strong/></p>
     *
     * @param clz 类型class
     * @return {@link T} 实例
     * @throws BeansException 异常
     */
    public static <T> T createBean(Class<T> clz) throws BeansException {
        return beanFactory.createBean(clz);
    }

    /**
     * 销毁Bean
     *
     * @param bean 实例
     */
    public static void destroyBean(Object bean){
        beanFactory.destroyBean(bean);
    }

    /**
     * 获取单例Bean的数量
     */
    public static int getSingletonCount(){
        return beanFactory.getSingletonCount();
    }
}
