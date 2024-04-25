package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.base.ClientLanguage;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.Internationalized;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.CollectionUtils;
import cn.acyou.leo.framework.util.ReflectUtils;
import cn.acyou.leo.framework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 国际化切面
 *  example:
 *  INSERT INTO scorpio.t_dict (id, name, code, parent_id, sort, remark, status, international_lang, create_time, update_time)
 *  VALUES(2155, '计量单位', 'unit', 0, 1, NULL, 1, '{''name_en'':''unit'',''name_jp'':''けいりょうたんい''}', '2024-04-25 15:09:57', '2024-04-25 16:05:51');
 *
 *  说明：通过international_lang字段固定配置，不过存在问题：**搜索时无法使用字段搜索**
 * @author youfang
 * @version [1.0.0, 2024/4/25 15:15]
 **/
@Slf4j
@Aspect
@Component
public class InternationalizedAspect {

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void pointCutCtl() {

    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointCutRestCtl() {

    }

    @Around("pointCutCtl() || pointCutRestCtl()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        try {
            if (proceed instanceof Result) {
                Result<?> r = (Result<?>) proceed;
                Object data = r.getData();
                if (data instanceof Internationalized) {
                    processInternationalizedData((Internationalized) data);
                }
                if (data instanceof PageData) {
                    List<?> list = ((PageData<?>) data).getList();
                    if (CollectionUtils.isNotEmpty(list) && list.get(0) instanceof Internationalized) {
                        for (Object datum : list) {
                            processInternationalizedData((Internationalized) datum);
                        }
                    }
                }
                if (data instanceof Collection) {
                    Collection<?> collection = (Collection<?>) data;
                    if (CollectionUtils.isNotEmpty(collection) && collection.stream().findFirst().get() instanceof Internationalized) {
                        for (Object datum : (Collection<?>) data) {
                            processInternationalizedData((Internationalized) datum);
                        }
                    }

                }
            }
        }catch (Exception e) {
            //ignore
        }

        return proceed;
    }

    public void processInternationalizedData(Internationalized obj){
        String internationalLang = obj.getInternationalLang();
        if (StringUtils.isNotBlank(internationalLang)) {
            JSONObject jsonObject = JSON.parseObject(internationalLang);
            ClientLanguage clientLanguage = AppContext.getClientLanguage();
            String clientLanguageName = clientLanguage.getName();
            ReflectionUtils.doWithFields(obj.getClass(), (t)->{
                String fieldName = t.getName();
                String v = jsonObject.getString(fieldName + "_" + clientLanguageName);
                if (StringUtils.isNotBlank(v)) {
                    ReflectUtils.setFieldValue(t, obj, v);;
                }
            });
        }
    }
}
