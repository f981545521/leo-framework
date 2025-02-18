package cn.acyou.leo.product.dto.so;

import cn.acyou.leo.framework.model.PageSo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-03 15:50]
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSo extends PageSo {

    @NotBlank(message = "姓名不能为空！")
    private String	name;

    @NotNull(message = "年龄不能为空！")
    @Min(value = 0, message =  "年龄不能小于0岁！")
    @Max(value = 200, message =  "年龄不能大于200岁！")
    private Integer age;
}
