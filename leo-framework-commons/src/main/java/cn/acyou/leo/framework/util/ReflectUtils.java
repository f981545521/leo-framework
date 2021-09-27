package cn.acyou.leo.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020-3-21 下午 10:05]
 **/
public class ReflectUtils {

    /**
     * 递归寻找Field
     *
     * @param annotatedClass  clazz
     * @param annotationClass 注解
     * @return {@link Field}
     */
    public static Field recursiveFieldFinder(Class<?> annotatedClass,
                                             Class<? extends Annotation> annotationClass) {
        for (Field f : annotatedClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(annotationClass)) {
                return f;
            }
        }
        if (annotatedClass.getSuperclass() != null) {
            return recursiveFieldFinder(annotatedClass.getSuperclass(),
                    annotationClass);
        }
        return null;
    }

    /**
     * 获取字段
     * 根据属性名获取属性
     *
     * @param fieldName 字段名
     * @param clazz     clazz
     * @return {@link Field}
     */
    public static Field getField(String fieldName, Class<?> clazz) {
        Class<?> old = clazz;
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                if (field != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        if (field == null) {
            throw new NullPointerException(old + "没有" + fieldName + "属性");
        }
        return field;
    }

    /**
     * 获取字段
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
     * 获取字段
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
     * 获取字段
     * 获取当前类的属性 包括父类
     *
     * @param clazz     clazz
     * @param stopClass 停止类
     * @return list
     */
    public static List<Field> getFields(Class<?> clazz, Class<?> stopClass) {
        try {
            List<Field> fieldList = new ArrayList<>();
            while (clazz != null && clazz != stopClass) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
            }
            return fieldList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取字段
     * 获取当前类的属性 包括父类
     *
     * @param clazz clazz
     * @return List
     */
    @Deprecated
    public static List<Field> getFields(Class<?> clazz) {
        return getFields(clazz, Object.class);
    }

    /**
     * 获得父类
     *
     * @param clazz     clazz
     * @param stopClass 停止类
     * @return List
     */
    @SuppressWarnings("unused")
    private static List<Class<?>> getSuperClasses(Class<?> clazz, Class<?> stopClass) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null && clazz != stopClass) {//当父类为null的时候说明到达了最上层的父类(Object类).
            classes.add(clazz);
            clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
        }
        return classes;
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
     * 设置字段值
     * 通过属性赋值
     *
     * @param field  场
     * @param object 对象
     * @param value  价值
     */
    public static void setFieldValue(Field field, Object object, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                field.set(object, value);
                field.setAccessible(false);
            } else {
                field.set(object, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取字段值
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
     * 获取字段值
     * 获取属性的值
     *
     * @param object 对象
     * @param field  场
     * @return {@link Object}
     */
    public static Object getFieldValue(Object object, Field field) {
        try {
            Object value;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                value = field.get(object);
                field.setAccessible(false);
            } else {
                value = field.get(object);
            }
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置值的设置方法
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
     * 设置值的设置方法
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
     * 通过get方法
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
     * 通过get方法
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
     * get方法
     * 获取某个类的某个方法(当前类和父类)
     *
     * @param methodName 方法名称
     * @param clazz      clazz
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, Class<?> clazz) {
        Method method = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName);
                break;
            } catch (Exception e) {
            }
        }
        if (method == null) {
            throw new NullPointerException("没有" + methodName + "方法");
        }
        return method;
    }

    /**
     * get方法的名字
     * 获取get方法
     *
     * @param fieldName 属性名
     * @return {@link String}
     */
    public static String getMethodName(String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return methodName;
    }

    /**
     * get方法
     * 获取某个类的某个方法(当前类和父类) 带一个参数
     *
     * @param methodName 方法名称
     * @param clazz      clazz
     * @param paramType  参数类型
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, Class<?> clazz, Class<?> paramType) {
        Method method = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, paramType);
                if (method != null) {
                    return method;
                }
            } catch (Exception e) {
            }
        }
        if (method == null) {
            throw new NullPointerException(clazz + "没有" + methodName + "方法");
        }
        return method;
    }

    /**
     * get方法
     * 获取某个类的某个方法(当前类和父类)
     *
     * @param methodName 方法名称
     * @param obj        obj
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, Object obj) {
        return getMethod(methodName, obj.getClass());
    }

    /**
     * get方法
     * 获取某个类的某个方法(当前类和父类) 一个参数
     *
     * @param methodName 方法名称
     * @param obj        obj
     * @param paramType  参数类型
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, Object obj, Class<?> paramType) {
        return getMethod(methodName, obj.getClass(), paramType);
    }

    /**
     * get方法
     * 获取某个类的某个方法(当前类和父类)
     *
     * @param methodName 方法名称
     * @param clazz      clazz
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, String clazz) {
        try {
            return getMethod(methodName, Class.forName(clazz));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * get方法
     * 获取某个类的某个方法(当前类和父类) 一个参数
     *
     * @param methodName 方法名称
     * @param clazz      clazz
     * @param paramType  参数类型
     * @return {@link Method}
     */
    public static Method getMethod(String methodName, String clazz, Class<?> paramType) {
        try {
            return getMethod(methodName, Class.forName(clazz), paramType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * get方法注释
     * 获取方法上的注解
     *
     * @param method                方法
     * @param targetAnnotationClass 目标注释类
     * @return {@link Annotation}
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Annotation getMethodAnnotation(Method method, Class targetAnnotationClass) {
        Annotation methodAnnotation = method.getAnnotation(targetAnnotationClass);
        return methodAnnotation;
    }

    /**
     * 获取字段注释
     * 获取属性上的注解
     *
     * @param field                 场
     * @param targetAnnotationClass 目标注释类
     * @return {@link Annotation}
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Annotation getFieldAnnotation(Field field, Class targetAnnotationClass) {
        Annotation methodAnnotation = field.getAnnotation(targetAnnotationClass);
        return methodAnnotation;
    }

    /**
     * 得到类注释
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
     * 得到类注释
     * 获取类上的注解
     *
     * @param targetAnnotationClass 目标注释类
     * @param obj                   obj
     * @return 目标注解实例
     */
    @SuppressWarnings("rawtypes")
    public static Annotation getClassAnnotation(Class targetAnnotationClass, Object obj) {
        return getClassAnnotation(targetAnnotationClass, obj.getClass());
    }

    /**
     * 得到类注释
     * 获取类上的注解
     *
     * @param targetAnnotationClass 目标注释类
     * @param clazz                 clazz
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
     * 得到注释的值
     * 获取注解某个属性的值
     *
     * @param methodName 属性名
     * @param annotation 目标注解
     * @return {@link T}
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

    /**
     * 判断 clazz是否是target的子类型或者相等
     */
    public static boolean isSubClassOrEquesClass(Class<?> clazz, Class<?> target) {
        if (clazz == target) {
            return true;
        }
        while (clazz != Object.class) {
            if (clazz == target) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    /**
     * 获取泛型类型
     *
     * @param clazz clazz
     * @param index 指数
     * @return {@link Class<?>}
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                return Object.class;
            }
        }
    }

}
