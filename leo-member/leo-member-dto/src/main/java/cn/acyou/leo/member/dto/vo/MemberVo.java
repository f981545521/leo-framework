package cn.acyou.leo.member.dto.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 18:14]
 */
@Data
public class MemberVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer memberId;

    private String memberName;

    private Integer stockNumber;

    private Integer status;
}
