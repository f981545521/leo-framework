package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.base.OrganizationVo;
import cn.acyou.leo.framework.constant.DataScopeConstant;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.DataPermissionDeniedException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 数据权限工具
 * <p>
 * 注意返回值会以and开头,如：
 * 可以获取 -> and create_user = 12
 * 可以获取 -> and org_id in (12, 13)
 * 如何使用：
 * <pre>
 *     {@code
 *    <select id="listXXX" resultType="cn.acyou.xxx.vo.xxx">
 *         select t.*
 *         from r_table_1 t
 *         <where>
 *             ${@cn.acyou.leo.framework.util.DataScopeUtil@scopeSql("t")}
 *         </where>
 *     </select>
 *     }
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020/8/17]
 **/
public class DataScopeUtil {

    /**
     * 获取数据权限sql
     *
     * @return {@link String}   数据权限sql
     */
    public static String scopeSql() {
        return scopeSql("");
    }

    /**
     * 获取数据权限sql，支持别名
     *
     * @param alias 别名
     * @return {@link String}   数据权限sql
     */
    public static String scopeSql(String alias) {
        String scopeSql = "";
        LoginUser loginUser = AppContext.getLoginUser();
        if (loginUser == null) {
            throw new DataPermissionDeniedException("您没有查看数据的权限，请先登录！");
        }
        Set<String> roleCodes = loginUser.getRoleCodes();
        //根据ROLE  dataScope 生成SQL
        if (roleCodes == null || roleCodes.isEmpty()) {
            throw new DataPermissionDeniedException("您没有查看数据的权限，请先添加角色！");
        }

        String actionUrl = AppContext.getActionUrl();
        int dataScope = loginUser.getRoleVo().getDataScope();
        String dataScopeCustomField = "";

        Long orgId = null;
        OrganizationVo organization = loginUser.getOrganization();

        if (organization != null) {
            orgId = organization.getOrgId();
        }
        StringBuilder sqlString = new StringBuilder();
        switch (dataScope) {
            case DataScopeConstant.DATA_SCOPE_ALL:
                break;
            case DataScopeConstant.DATA_SCOPE_STORAGE_AND_SUB:
                StringBuilder scopeDeptSubSql = new StringBuilder();
                if (StringUtils.isNotEmpty(alias)) {
                    scopeDeptSubSql.append(alias).append(".");
                }
                if (orgId == null) {
                    throw new DataPermissionDeniedException("您没有查看数据的权限，请先添加科室！");
                }
                List<Long> subOrgIds = organization.getSubOrgIds();
                scopeDeptSubSql.append("org_id in ( ").append(StringUtils.join(subOrgIds, ",")).append(")");
                sqlString.append(scopeDeptSubSql);
                break;
            case DataScopeConstant.DATA_SCOPE_STORAGE_ONLY:
                StringBuilder scopeDeptSql = new StringBuilder();
                if (StringUtils.isNotEmpty(alias)) {
                    scopeDeptSql.append(alias).append(".");
                }
                if (orgId == null) {
                    throw new DataPermissionDeniedException("您没有查看数据的权限，请先添加科室！");
                }
                scopeDeptSql.append("org_id = '").append(orgId).append("'");
                sqlString.append(scopeDeptSql);
                break;
            case DataScopeConstant.DATA_SCOPE_OWN:
                StringBuilder scopeOwnSql = new StringBuilder();
                if (StringUtils.isNotEmpty(alias)) {
                    scopeOwnSql.append(alias).append(".");
                }
                scopeOwnSql.append("create_user = ").append(loginUser.getUserId());
                sqlString.append(scopeOwnSql);
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(sqlString.toString())) {
            scopeSql = " AND (" + sqlString + ")";
        }
        return scopeSql;
    }

}
