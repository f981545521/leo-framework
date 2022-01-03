package cn.acyou.leo.framework.util;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展 反射工具类
 * @author youfang
 * @version [1.0.0, 2020-3-21 下午 10:05]
 **/
public class ReflectUtils extends ReflectionUtils {

    /**
     * 获取类中带有指定注解的Field
     *
     * @param annotatedClass  目标clazz
     * @param annotationClass 注解
     * @return {@link Field}
     */
    public static Field recursiveFieldFinder(Class<?> annotatedClass, Class<? extends Annotation> annotationClass) {
        List<Field> fieldList = new ArrayList<>();
        doWithFields(annotatedClass, fieldList::add, f-> f.isAnnotationPresent(annotationClass));
        if (!fieldList.isEmpty()) {
            return fieldList.get(0);
        }
        return null;
    }

    /**
     * 根据属性名获取Field
     *
     * @param fieldName 字段名
     * @param clazz     clazz
     * @return {@link Field}
     */
    public static Field getField(String fieldName, Class<?> clazz) {
        return findField(clazz, fieldName);
    }

    /**
     * 获取目标类的属性
     *
     * @param fieldName 字段名
     * @param className 类名
     * @return {@link Field}
     */
    public static Field getField(String fieldName, String className) {
        try {
            return getField(fieldName, Class.forName(className));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取目标对象的属性
     *
     * @param fieldName 字段名
     * @param object    对象
     * @return {@link Field}
     */
    public static Field getField(String fieldName, Object object) {
        return getField(fieldName, object.getClass());
    }

    /**
     * 获取当前类的所有属性 包括父类
     *
     * @param clazz clazz
     * @return {@link List}<{@link Field}>
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(clazz, fields::add);
        return fields;
    }

    /**
     * 设置字段值
     * 通过属性赋值
     *
     * @param fieldName 字段名
     * @param object    对象
     * @param value     价值
     */
    public static void setFieldValue(String fieldName, Object object, Object value) {
        Field field = getField(fieldName, object.getClass());
        setFieldValue(field, object, value);
    }

    /**
     * 通过属性赋值
     *
     * @param field  场
     * @param object 对象
     * @param value  价值
     */
    public static void setFieldValue(Field field, Object object, Object value) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, object, value);
    }

    /**
     * 获取属性的值
     *
     * @param object    对象
     * @param fieldName 字段名
     * @return {@link Object}
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getField(fieldName, object.getClass());
        return getFieldValue(object, field);
    }

    /**
     * 获取属性的值
     *
     * @param object 对象
     * @param field  场
     * @return {@link Object}
     */
    public static Object getFieldValue(Object object, Field field) {
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, object);
    }

    /**
     * 通过set方法赋值
     *
     * @param fieldName 字段名
     * @param object    对象
     * @param value     价值
     */
    public static void setValueBySetMethod(String fieldName, Object object, Object value) {
        if (object == null) {
            throw new RuntimeException("实例对象不能为空");
        }
        if (value == null) {
            return;
        }
        try {
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method setMethod = getMethod(setMethodName, object.getClass(), value.getClass());
            setMethod.invoke(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 通过set方法赋值
     *
     * @param field  场
     * @param object 对象
     * @param value  价值
     */
    public static void setValueBySetMethod(Field field, Object object, Object value) {
        if (object == null) {
            throw new RuntimeException("实例对象不能为空");
        }
        if (value == null) {
            return;
        }
        setValueBySetMethod(field.getName(), object, value);
    }

    /**
     * 通过get方法取值
     *
     * @param fieldName 字段名
     * @param object    对象
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueByGetMethod(String fieldName, Object object) {
        try {
            if (fieldName != null && fieldName.trim().length() > 0) {
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = getMethod(getMethodName, object.getClass());
                return (T) getMethod.invoke(object);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 通过get方法取值
     *
     * @param field  场
     * @param object 对象
     * @return {@link T}
     */
    public static <T> T getValueByGetMethod(Field field, Object object) {
        return getValueByGetMethod(field.getName(), object);
    }

    /**
     * 获取某个类的某个方法(当前类和父类)
     */
    public static Method getMethod(String methodName, Class<?> clazz) {
        return findMethod(clazz, methodName);
    }

    /**
     * 获取get方法
     *
     * @param fieldName 属性名
     * @return getXXX
     */
    public static String getMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 获取某个类的某个方法(当前类和父类) 带一个参数
     */
    public static Method getMethod(String methodName, Class<?> clazz, Class<?> paramType) {
        return findMethod(clazz, methodName, paramType);
    }

    /**
     * 获取某个类的某个方法(当前类和父类)
     */
    public static Method getMethod(String methodName, Object obj) {
        return getMethod(methodName, obj.getClass());
    }

    /**
     * 获取某个类的某个方法(当前类和父类) 一个参数
     */
    public static Method getMethod(String methodName, Object obj, Class<?> paramType) {
        return getMethod(methodName, obj.getClass(), paramType);
    }

    /**
     * 获取某个类的某个方法(当前类和父类)
     */
    public static Method getMethod(String methodName, String clazz) {
        try {
            return getMethod(methodName, Class.forName(clazz));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取某个类的某个方法(当前类和父类) 一个参数
     */
    public static Method getMethod(String methodName, String clazz, Class<?> paramType) {
        try {
            return getMethod(methodName, Class.forName(clazz), paramType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取方法上的注解
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Annotation getMethodAnnotation(Method method, Class targetAnnotationClass) {
        return method.getAnnotation(targetAnnotationClass);
    }

    /**
     * 获取属性上的注解
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Annotation getFieldAnnotation(Field field, Class targetAnnotationClass) {
        return field.getAnnotation(targetAnnotationClass);
    }

    /**
     * 获取类上的注解
     *
     * @param targetAnnotationClass 目标注解
     * @param targetObjcetClass     目标类
     * @return 目标注解实例
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Annotation getClassAnnotation(Class targetAnnotationClass, Class<?> targetObjcetClass) {
        Annotation methodAnnotation = targetObjcetClass.getAnnotation(targetAnnotationClass);
        return methodAnnotation;
    }

    /**
     * 获取类上的注解
     *
     * @return 目标注解实例
     */
    @SuppressWarnings("rawtypes")
    public static Annotation getClassAnnotation(Class targetAnnotationClass, Object obj) {
        return getClassAnnotation(targetAnnotationClass, obj.getClass());
    }

    /**
     * 获取类上的注解
     *
     * @return 目标注解实例
     */
    @SuppressWarnings("rawtypes")
    public static Annotation getClassAnnotation(Class targetAnnotationClass, String clazz) {
        try {
            return getClassAnnotation(targetAnnotationClass, Class.forName(clazz));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取注解某个属性的值
     *
     * @param methodName 属性名
     * @param annotation 目标注解
     * @param <T>        返回类型
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAnnotationValue(String methodName, Annotation annotation) {
        try {
            Method method = annotation.annotationType().getMethod(methodName);
            Object object = method.invoke(annotation);
            return (T) object;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取某个类的某个方法上的某个注解的属性
     *
     * @param methodName            注解属性的名字
     * @param targetAnnotationClass 目标注解
     * @param targetObjecMethodName 目标类的方法
     * @param targetObjectClass     目标类
     * @param <T>                   返回值类型
     */
    @SuppressWarnings("rawtypes")
    public static <T> T getMethodAnnotationValue(String methodName, Class targetAnnotationClass,
                                                 String targetObjecMethodName, Class targetObjectClass) {
        Method method = getMethod(targetObjecMethodName, targetObjectClass);
        Annotation annotation = getMethodAnnotation(method, targetAnnotationClass);
        return getAnnotationValue(methodName, annotation);
    }

    /**
     * @param methodName            注解属性名
     * @param targetAnnotationClass 目标注解
     * @param targetObjecFieldName  目标属性名字
     * @param targetObjectClass     目标类
     * @param <T>                   返回值类型
     */
    @SuppressWarnings("rawtypes")
    public static <T> T getFieldAnnotationValue(String methodName, Class targetAnnotationClass,
                                                String targetObjecFieldName, Class targetObjectClass) {
        Field field = getField(targetObjecFieldName, targetObjectClass);
        Annotation annotation = getFieldAnnotation(field, targetAnnotationClass);
        return getAnnotationValue(methodName, annotation);
    }

}
