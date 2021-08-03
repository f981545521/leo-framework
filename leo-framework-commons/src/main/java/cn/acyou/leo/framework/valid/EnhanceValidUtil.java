package cn.acyou.leo.framework.valid;

import cn.acyou.leo.framework.annotation.valid.BaseValid;
import cn.acyou.leo.framework.annotation.valid.DateValidType;
import cn.acyou.leo.framework.annotation.valid.EnhanceValid;
import cn.acyou.leo.framework.annotation.valid.RegexType;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.service.DictValidService;
import cn.acyou.leo.framework.util.IdCardValidUtil;
import cn.acyou.leo.framework.util.ReflectUtils;
import cn.acyou.leo.framework.util.RegexUtil;
import cn.acyou.leo.framework.util.SpringHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 增强实体参数校验
 *
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class EnhanceValidUtil {
    private static final Logger logger = LoggerFactory.getLogger(EnhanceValidUtil.class);

    /**
     * 增强实体参数校验
     * <p>
     * 支持集合类型：List、Set
     * <pre>
     *      public Result<?> skuPage(@ParamValid @RequestBody List<GoodsScatteredProcessingReq> processingReq)
     * </pre>
     * 支持单个实体
     * <pre>
     *     public Result<?> updateInfo(@ParamValid @RequestBody GoodsUpdateReq goodsUpdateReq)
     * </pre>
     *
     * @param object 校验对象
     */
    public static void valid(Object object) {
        if (object instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) object;
            collection.forEach(EnhanceValidUtil::validEntity);
        } else {
            validEntity(object);
        }
    }

    /**
     * 校验实体
     *
     * @param object 对象
     */
    private static void validEntity(Object object) {
        //获取object的类型
        Class<?> clazz = object.getClass();
        //获取该类型声明的成员
        Field[] fields = clazz.getDeclaredFields();
        //遍历属性
        for (Field field : fields) {
            //对于private私有化的成员变量，通过setAccessible来修改器访问权限
            field.setAccessible(true);
            enhanceValidSupport(field, object);
            //重新设置会私有权限
            field.setAccessible(false);
        }
    }

    /**
     * 验证注解
     *
     * @param field  字段
     * @param object 校验对象
     */
    private static void enhanceValidSupport(Field field, Object object) {
        //获取对象的成员的 增强校验注解信息
        EnhanceValid enhanceValid = field.getAnnotation(EnhanceValid.class);
        //未加注解的字段直接返回
        if (enhanceValid != null) {
            BaseValid[] baseValid = enhanceValid.value();
            for (BaseValid validField : baseValid) {
                baseValidSupport(field, object, validField);
            }
        }
        //获取对象的成员的 基本校验注解信息
        BaseValid baseValid = field.getAnnotation(BaseValid.class);
        //未加注解的字段直接返回
        if (baseValid != null) {
            baseValidSupport(field, object, baseValid);
        }
    }

    /**
     * 验证基础类型
     *
     * @param field     字段
     * @param object    校验对象
     * @param baseValid 验证注解
     */
    private static void baseValidSupport(Field field, Object object, BaseValid baseValid) {
        //当前值
        Object validValue = null;
        String description = "请求参数错误，请检查！";
        try {
            validValue = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //当前字段名称
        String currentFieldName = field.getName();
        //自定义描述
        if (StringUtils.isNotEmpty(baseValid.message())) {
            description = baseValid.message();
        }

        /* *********** 注解解析工作开始 ***************** */
        //非NULL
        if (baseValid.notNull()) {
            if (validValue == null || StringUtils.isEmpty(validValue.toString())) {
                throw new ServiceException(description);
            }
        }

        if (baseValid.notEmpty()) {
            if (validValue == null || StringUtils.isEmpty(validValue.toString())) {
                logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数为NULL");
                throw new ServiceException(description);
            }
            if (validValue instanceof Collection && ((Collection<?>) validValue).isEmpty()) {
                logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "集合为空");
                throw new ServiceException(description);
            }
            if (validValue instanceof Map && ((Map<?, ?>) validValue).isEmpty()) {
                logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "集合为空");
                throw new ServiceException(description);
            }
        }

        //非NULL 进一步校验
        if (validValue != null && !"".equals(validValue.toString())) {

            if (validValue.toString().length() > baseValid.maxLength() && baseValid.maxLength() != 0) {
                logger.warn("[数据校验]|{}|{}|{}|{}", "valid failed", currentFieldName, "长度超过了", baseValid.maxLength());
                throw new ServiceException(description);
            }

            if (validValue.toString().length() < baseValid.minLength() && baseValid.minLength() != 0) {
                logger.warn("[数据校验]|{}|{}|{}|{}", "valid failed", currentFieldName, "长度小于了", baseValid.minLength());
                throw new ServiceException(description);
            }
            if (baseValid.fixLength() != 0  && validValue.toString().length() < baseValid.fixLength()) {
                logger.warn("[数据校验]|{}|{}|{}|{}", "valid failed", currentFieldName, "固定长度为", baseValid.fixLength());
                throw new ServiceException(description);
            }

            if (NumberUtils.isCreatable(validValue.toString()) && baseValid.min() != Integer.MIN_VALUE
                    && Integer.parseInt(validValue.toString()) < baseValid.min()) {
                logger.warn("[数据校验]|{}|{}|{}|{}", "valid failed", currentFieldName, "不能小于", baseValid.min());
                throw new ServiceException(description);
            }

            if (NumberUtils.isCreatable(validValue.toString()) && baseValid.max() != Integer.MIN_VALUE
                    && Integer.parseInt(validValue.toString()) > baseValid.max()) {
                logger.warn("[数据校验]|{}|{}|{}|{}", "valid failed", currentFieldName, "不能大于", baseValid.max());
                throw new ServiceException(description);
            }

            if (baseValid.notInRange().length > 0) {
                if (!ArrayUtils.contains(baseValid.notInRange(), validValue.toString())) {
                    logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "取值不在范围内");
                    throw new ServiceException(description);
                }
            }

            if (baseValid.numberNotInRange().length > 0) {
                if (!ArrayUtils.contains(baseValid.numberNotInRange(), Integer.parseInt(validValue.toString()))) {
                    logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "取值不在范围内");
                    throw new ServiceException(description);
                }
            }

            if (baseValid.dictCode().length() > 0) {
                String dictCode = baseValid.dictCode();
                String value = validValue.toString();
                DictValidService dictValidService = SpringHelper.getBean(DictValidService.class);
                boolean isExist = dictValidService.validDictValue(dictCode, value);
                if (!isExist) {
                    logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "数据字典不在范围内！");
                    throw new ServiceException(description);
                }
            }

            if (baseValid.regexType() != RegexType.NONE) {
                String result = null;
                switch (baseValid.regexType()) {
                    case SPECIALCHAR:
                        if (RegexUtil.hasSpecialChar(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：不能含有特殊字符！");
                            result = description;
                        }
                        break;
                    case CHINESE:
                        if (RegexUtil.isChinese2(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：不能含有中文字符！");
                            result = description;
                        }
                        break;
                    case EMAIL:
                        if (!RegexUtil.isEmail(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：邮箱地址格式不正确！");
                            result = description;
                        }
                        break;
                    case IP:
                        if (!RegexUtil.isIp(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：IP地址格式不正确！");
                            result = description;
                        }
                        break;
                    case NUMBER:
                        if (!RegexUtil.isNumber(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：不是数字！");
                            result = description;
                        }
                        break;
                    case MOBILE_PHONE:
                        if (!RegexUtil.isMobilePhone(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：手机号码格式不正确！");
                            result = description;
                        }
                        break;
                    case MOBILEPHONE_OR_TELEPHONE:
                        if (!RegexUtil.isTelephoneOrMobilephone(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：号码格式不正确！");
                            result = description;
                        }
                        break;
                    case ID_CARD:
                        if (!IdCardValidUtil.isValidatedAllIdcard(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：身份证号码格式不正确！");
                            result = description;
                        }
                        break;
                    case DATE:
                        if (!RegexUtil.isDateStr(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：日期格式不正确！");
                            result = description;
                        }
                        break;
                    case DATETIME:
                        if (!RegexUtil.isDateTime(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：时间格式不正确！");
                            result = description;
                        }
                        break;
                    case NUMBER_LETTER_COMBINATION:
                        if (!RegexUtil.isNumberLetter(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：(数字与字母)格式不正确！");
                            result = description;
                        }
                        break;
                    case NUMBER_LETTER_SYMBOL_COMBINATION:
                        if (!RegexUtil.isNumberLetterSymbol(validValue.toString())) {
                            logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "参数校验错误：(数字与字母与符号)格式不正确！");
                            result = description;
                        }
                        break;
                    default:
                        break;
                }
                if (null != result) {
                    throw new ServiceException(result);
                }
            }

            if (baseValid.dateValid() != DateValidType.none) {
                String result = null;
                if (validValue instanceof Date) {
                    Date validValueDate = (Date) validValue;
                    switch (baseValid.dateValid()) {
                        case if_afterNow:
                            if (validValueDate.after(new Date())) {
                                logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "不能大于当前时间！");
                                result = description;
                            }
                            break;
                        case if_beforeNow:
                            if (validValueDate.before(new Date())) {
                                logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "不能小于当前时间！");
                                result = description;
                            }
                            break;
                        case if_afterSpecifyDate:
                            String afterDateFieldName = baseValid.specifyDateFieldName();
                            if (afterDateFieldName.trim().length() > 0) {
                                Object fieldValue = ReflectUtils.getFieldValue(object, afterDateFieldName);
                                if (fieldValue instanceof Date) {
                                    Date filedDate = (Date) fieldValue;
                                    if (validValueDate.before(filedDate)) {
                                        logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "不能大于" + afterDateFieldName + "！");
                                        result = description;
                                    }
                                }
                            }
                            break;
                        case if_beforeSpecifyDate:
                            String beforeDateFieldName = baseValid.specifyDateFieldName();
                            if (beforeDateFieldName.trim().length() > 0) {
                                Object fieldValue = ReflectUtils.getFieldValue(object, beforeDateFieldName);
                                if (fieldValue instanceof Date) {
                                    Date filedDate = (Date) fieldValue;
                                    if (validValueDate.before(filedDate)) {
                                        logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "不能小于" + beforeDateFieldName + "！");
                                        result = description;
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    if (null != result) {
                        throw new ServiceException(result);
                    }

                }
            }

            if (StringUtils.isNotEmpty(baseValid.regexExpression())) {
                if (validValue.toString().matches(baseValid.regexExpression())) {
                    logger.warn("[数据校验]|{}|{}|{}", "valid failed", currentFieldName, "格式不正确");
                    throw new ServiceException(description);
                }
            }

            //实体类型 继续校验
            if (baseValid.entityValid()) {
                valid(validValue);
            }

            //实体集合实体 继续校验
            if (baseValid.entityCollectionValid()) {
                if (validValue instanceof Collection) {
                    Collection<?> valueList = (Collection<?>) validValue;
                    for (Object o : valueList) {
                        valid(o);
                    }
                }
            }
        }
        /* ***********注解解析工作结束***************** */
    }


}
