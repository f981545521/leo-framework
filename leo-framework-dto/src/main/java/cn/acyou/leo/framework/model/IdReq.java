package cn.acyou.leo.framework.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-10]
 **/
@Data
public class IdReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
}
