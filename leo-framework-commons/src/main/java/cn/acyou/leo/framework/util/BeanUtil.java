package cn.acyou.leo.framework.util;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanUtil {

    /**
     * 将Object 转换为Map key-属性名 value-属性值
     * @param obj bean
     * @return Map key-属性名 value-属性值
     */
    public static HashMap<String, Object> convertToMap(Object obj) {
        HashMap<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            if ("serialVersionUID".equals(varName)) {
                continue;
            }
            boolean accessFlag = field.isAccessible();
            field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o != null){
                map.put(varName, o.toString());
            }
            field.setAccessible(accessFlag);
        }
        return map;
    }

    /**
     * Bean 不同的信息
     *
     *  ex: {姓名=王二 to 王xiao二, 年龄=3 to 12}
     * @param obj1 obj1
     * @param obj2 obj2
     * @return Map
     */
    public static Map<String, String> differentInfo(Object obj1, Object obj2){
        Map<String, String> resMap = new LinkedHashMap<>();
        if (!obj1.getClass().equals(obj2.getClass())) {
            return resMap;
        }
        Field[] obj1Fields = obj1.getClass().getDeclaredFields();
        Field[] obj2Fields = obj2.getClass().getDeclaredFields();
        for (Field obj1Field : obj1Fields) {
            ApiModelProperty annotation = obj1Field.getAnnotation(ApiModelProperty.class);
            String value = annotation.value();
            try {
                obj1Field.setAccessible(true);
                Object o1V = obj1Field.get(obj1);
                obj1Field.setAccessible(false);
                Object o2V = null;
                for (Field obj2Field : obj2Fields) {
                    if (obj1Field.getName().equals(obj2Field.getName())) {
                        obj2Field.setAccessible(true);
                        o2V = obj2Field.get(obj2);
                        obj2Field.setAccessible(false);
                        break;
                    }
                }
                String o1Vstr = o1V == null ? "" : o1V.toString();
                String o2Vstr = o2V == null ? "" : o2V.toString();
                if (!o1Vstr.equals(o2Vstr)){
                    String desc = o1Vstr + " -> " + o2Vstr;
                    resMap.put(value , desc);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resMap;
    }
}
