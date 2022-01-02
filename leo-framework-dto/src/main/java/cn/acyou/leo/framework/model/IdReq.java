package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.model.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-10]
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class IdReq extends DTO {

    @NotNull(message = "参数错误，请检查！")
    private Long id;
}
