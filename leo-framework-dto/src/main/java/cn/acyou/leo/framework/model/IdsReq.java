package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.model.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/6]
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class IdsReq extends DTO {

    @NotEmpty(message = "参数错误，请检查！")
    private List<Long> ids;
}
