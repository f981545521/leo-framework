package cn.acyou.leo.framework.base;

import lombok.Data;

import java.util.List;

/**
 * 组织
 * @author fangyou
 * @version [1.0.0, 2021-09-28 10:32]
 */
@Data
public class OrganizationVo {

    private Long orgId;

    private List<Long> subOrgIds;

}
