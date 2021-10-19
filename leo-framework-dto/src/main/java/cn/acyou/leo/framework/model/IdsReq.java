package cn.acyou.leo.framework.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/6]
 **/
@Data
public class IdsReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "参数错误，请检查！")
    private List<Long> ids;
}
