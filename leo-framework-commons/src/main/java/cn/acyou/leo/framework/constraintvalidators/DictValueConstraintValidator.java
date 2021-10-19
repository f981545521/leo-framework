package cn.acyou.leo.framework.constraintvalidators;

import cn.acyou.leo.framework.annotation.valid.DictValue;
import cn.acyou.leo.framework.service.DictValidService;
import cn.acyou.leo.framework.util.SpringHelper;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
@Slf4j
public class DictValueConstraintValidator implements ConstraintValidator<DictValue, Object> {

    private String dictCode;

    @Override
    public void initialize(DictValue constraintAnnotation) {
        dictCode = constraintAnnotation.dictCode();
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
        if (dictCode != null && dictCode.length() > 0) {
            DictValidService dictValidService = SpringHelper.getBean(DictValidService.class);
            return dictValidService.validDictValue(dictCode, value.toString());
        }
        return true;
    }
}
