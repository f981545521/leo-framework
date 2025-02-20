package cn.acyou.leo.member.client;

import cn.acyou.leo.member.dto.vo.MemberVo;

/**
 * @author youfang
 * @version [1.0.0, 2025/2/20 18:06]
 **/
public interface MemberClient {

    MemberVo getMemberById(Long memberId);
}
