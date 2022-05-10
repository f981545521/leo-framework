package cn.acyou.leo.pay.dto;

import cn.acyou.leo.framework.annotation.valid.ListValue;
import cn.acyou.leo.framework.annotation.valid.PropertyScriptAssert;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2021-10-17]
 **/
@Data
//Class级别的校验执行会优先与属性级别
@PropertyScriptAssert(script = "_this.password==_this.confirmation", message = "密码输入不一致！")
public class StudentReq implements Serializable {
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "兴趣不能为空")
    @Size(min = 2, message = "兴趣至少填写2项")
    private List<String> interests;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 200, message = "年龄不能大于200")
    private Integer age;

    @ListValue(strValues = {"Y", "N"}, message = "启用状态不正确")
    private String enable;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String confirmation;
}
