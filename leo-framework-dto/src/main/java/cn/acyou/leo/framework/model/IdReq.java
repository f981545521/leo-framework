package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.annotation.valid.BaseValid;
import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-10]
 **/
@Data
public class IdReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @BaseValid(notNull = true, message = "参数错误，请检查！")
    private Long id;
}
