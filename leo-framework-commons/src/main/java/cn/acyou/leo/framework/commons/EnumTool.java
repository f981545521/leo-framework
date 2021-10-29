package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.base.EnumEntity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <pre>
 *         List<EnumEntity> enumEntities = Constant.SysRole.tool().listAllField();
 *         System.out.println(enumEntities);
 * </pre>
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class EnumTool {
    protected Class<?> clazz;

    public static EnumTool newInstance(Class<?> clazz){
        return new EnumTool(clazz);
    }

    public EnumTool(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * 列出所有字段
     *
     * @return {@link List < EnumEntity >}
     */
    public List<EnumEntity> listAllField() {
        List<EnumEntity> resultList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(clazz);
                if (o instanceof EnumEntity) {
                    resultList.add((EnumEntity) o);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }


    /**
     * 获取字段名
     *
     * @param code code
     * @return {@link String}
     */
    public String getFieldName(String code) {
        List<EnumEntity> statusEntities = listAllField();
        for (EnumEntity statusEntity : statusEntities) {
            if (code.equals(statusEntity.getCode())) {
                return statusEntity.getName();
            }
        }
        return null;
    }
    /**
     * 获取字段名
     *
     * @param code code
     * @return {@link String}
     */
    public String getFieldName(Integer code) {
        return getFieldName(code.toString());
    }

}
