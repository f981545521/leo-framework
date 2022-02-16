package cn.acyou.leo.pay.dto;

import cn.acyou.leo.framework.annotation.valid.ListValue;
import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
@Data
@PropertyScriptAssert(script = "_this.password==_this.confirmation", message = "密码输入不一致！")
//@ScriptAssert(script = "_this.password==_this.confirmation", message = "密码输入不一致！", lang = "javascript")
public class StudentReq implements Serializable {
    @NotNull(message = "name 不能为空！")
    private String name;

    @NotNull(message = "interests 不能为空！")
    @Size(min = 1, message = "interests 取值不正确")
    private List<String> interests;

    @ListValue(values = {1,2,3}, message = "age 取值不正确")
    private Integer age;

    @ListValue(strValues = {"Y","N"}, message = "enable 取值不正确")
    private String enable;

    private String password;

    private String confirmation;
}
