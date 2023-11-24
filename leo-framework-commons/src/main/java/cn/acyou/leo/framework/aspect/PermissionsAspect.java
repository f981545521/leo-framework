package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.annotation.authz.Logical;
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

import java.util.Set;

/**
 *
 * 权限校验切面
 *
 * use support {@link RequiresRoles} & {@link RequiresPermissions}
 *
 * @author fangyou
 * @version [1.0.0, 2021-09-27 15:28]
 */
@Slf4j
@Aspect
@Component
public class PermissionsAspect {

    @Around("@annotation(cn.acyou.leo.framework.annotation.authz.RequiresRoles) || @annotation(cn.acyou.leo.framework.annotation.authz.RequiresPermissions)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RequiresRoles requiresRoles = methodSignature.getMethod().getAnnotation(RequiresRoles.class);
        RequiresPermissions requiresPermissions = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
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
                boolean containsAll = permsList.containsAll(Sets.newHashSet(needPermissions));
                if (!containsAll) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
            if (logical.equals(Logical.OR)) {
                boolean containsAny = CollectionUtils.containsAny(permsList, Sets.newHashSet(needPermissions));
                if (!containsAny) {
                    throw new ServiceException(CommonErrorEnum.E_INSUFFICIENT_PERMISSIONS);
                }
            }
        }
        return joinPoint.proceed();
    }
}
