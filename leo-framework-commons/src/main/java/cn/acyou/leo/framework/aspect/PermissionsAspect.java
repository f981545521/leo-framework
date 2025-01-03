package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.annotation.authz.Logical;
import cn.acyou.leo.framework.annotation.authz.RequiresLogin;
import cn.acyou.leo.framework.annotation.authz.RequiresPermissions;
import cn.acyou.leo.framework.annotation.authz.RequiresRoles;
import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.ServiceException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

/**
 *
 * 权限校验切面
 *
 * use support {@link RequiresLogin} & {@link RequiresRoles} & {@link RequiresPermissions}
 *
 * @author fangyou
 * @version [1.0.0, 2021-09-27 15:28]
 */
@Slf4j
//@Aspect
//@Component
public class PermissionsAspect {

    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    @Around("@annotation(cn.acyou.leo.framework.annotation.authz.RequiresLogin) || @annotation(cn.acyou.leo.framework.annotation.authz.RequiresRoles) || @annotation(cn.acyou.leo.framework.annotation.authz.RequiresPermissions)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RequiresLogin requiresLogin = methodSignature.getMethod().getAnnotation(RequiresLogin.class);
        RequiresRoles requiresRoles = methodSignature.getMethod().getAnnotation(RequiresRoles.class);
        RequiresPermissions requiresPermissions = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
        //登录
        if (requiresLogin != null) {
            LoginUser loginUser = AppContext.getLoginUser();
            if (loginUser == null) {
                throw new ServiceException(CommonErrorEnum.E_UNAUTHENTICATED);
            }
        }
        //角色
        if (requiresRoles != null) {
            LoginUser loginUser = AppContext.getLoginUser();
            if (loginUser == null) {
                throw new ServiceException(CommonErrorEnum.E_UNAUTHENTICATED);
            }
            Set<String> roleCodes = loginUser.getRoleCodes();
            Logical logical = requiresRoles.logical();
            String[] needRoles = requiresRoles.value();
            if (logical.equals(Logical.AND)) {
                boolean containsAll = roleCodes.containsAll(Sets.newHashSet(needRoles));
                if (!containsAll) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
            if (logical.equals(Logical.OR)) {
                boolean containsAny = CollectionUtils.containsAny(roleCodes, Sets.newHashSet(needRoles));
                if (!containsAny) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
        }
        //权限
        if (requiresPermissions != null) {
            LoginUser loginUser = AppContext.getLoginUser();
            if (loginUser == null) {
                throw new ServiceException(CommonErrorEnum.E_UNAUTHENTICATED);
            }
            Set<String> permsList = loginUser.getPermsList();
            Logical logical = requiresPermissions.logical();
            String[] needPermissions = requiresPermissions.value();
            if (logical.equals(Logical.AND)) {
                boolean containsAll = checkPermsAnd(permsList, needPermissions);
                if (!containsAll) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
            if (logical.equals(Logical.OR)) {
                boolean containsAny = checkPermsOr(permsList, needPermissions);
                if (!containsAny) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 验证用户是否含有指定权限，必须全部拥有
     *
     * @param permissions 权限列表
     */
    public boolean checkPermsAnd(Set<String> permissionList, String... permissions) {
        for (String permission : permissions) {
            if (!hasPermi(permissionList, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     *
     * @param permissions 权限码数组
     */
    public boolean checkPermsOr(Set<String> permissionList, String... permissions) {
        for (String permission : permissions) {
            if (hasPermi(permissionList, permission)) {
                return true;
            }
        }
        return permissions.length <= 0;
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }
}
