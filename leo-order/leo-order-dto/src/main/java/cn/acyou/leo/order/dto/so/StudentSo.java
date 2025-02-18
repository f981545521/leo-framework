package cn.acyou.leo.order.dto.so;

import cn.acyou.leo.framework.model.PageSo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-03 15:50]
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSo extends PageSo {

    private String	name;

    private Integer age;
}
