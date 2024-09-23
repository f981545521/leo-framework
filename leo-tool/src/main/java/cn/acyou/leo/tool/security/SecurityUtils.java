package cn.acyou.leo.tool.security;

import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/23 13:43]
 **/
public class SecurityUtils {


    public static String getToken(HttpServletRequest request){
        String token = request.getHeader(Constant.HeaderEnum.TOKEN_NAME);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(Constant.HeaderEnum.TOKEN_NAME);
        }
        return token;
    }

    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
