package cn.acyou.leo.framework.constraintvalidators;

import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scripting.support.StandardScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;

import javax.script.ScriptEngineManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本类级别校验 （使用javascript校验）
 * <pre>
 * &#064;PropertyScriptAssert(script = "_this.password==_this.confirmation", message = "密码输入不一致！"))
 * public class StudentReq {
 *     private String password;
 *     private String confirmation;
 * }
 * </pre>
 * @author fangyou
 * @version [1.0.0, 2021-10-18 13:45]
 */
@Slf4j
public class PropertyScriptAssertValidator implements ConstraintValidator<PropertyScriptAssert, Object> {

    private String script;

    public void initialize(PropertyScriptAssert constraintAnnotation) {
        this.script = constraintAnnotation.script();
    }

    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("_this", value);
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            StaticScriptSource staticScriptSource = new StaticScriptSource(script);
            StandardScriptEvaluator scriptEvaluator = new StandardScriptEvaluator(scriptEngineManager);
            scriptEvaluator.setLanguage("javascript");
            scriptEvaluator.setGlobalBindings(objectMap);
            Object result = scriptEvaluator.evaluate(staticScriptSource);
            if (result != null) {
                return (boolean) result;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


}
