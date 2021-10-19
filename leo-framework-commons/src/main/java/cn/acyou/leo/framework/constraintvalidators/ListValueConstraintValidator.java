package cn.acyou.leo.framework.constraintvalidators;


import cn.acyou.leo.framework.annotation.valid.ListValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Object> {

    private Set<Integer> rangeValues = new HashSet<>();
    private Set<String> strRangeValues = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        //获取前端需要校验数据
        int[] values = constraintAnnotation.values();
        String[] strValues = constraintAnnotation.strValues();
        for (int val : values) {
            rangeValues.add(val);
        }
        strRangeValues.addAll(Arrays.asList(strValues));
    }

    /**
     * 判断是否校验成功
     *
     * @param value   需要校验的值
     * @param context context
     * @return 是否通过
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null) {
            if (value instanceof String) {
                return strRangeValues.contains(value);
            }else {
                return rangeValues.contains(value);
            }
        }
        return true;
    }
}
