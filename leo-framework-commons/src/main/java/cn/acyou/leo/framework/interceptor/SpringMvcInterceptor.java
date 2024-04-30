package cn.acyou.leo.framework.interceptor;

import cn.acyou.leo.framework.constant.Constant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class SpringMvcInterceptor extends BaseInterceptor {

    @Override
    protected String getToken(HttpServletRequest request) {
        return request.getHeader(Constant.HeaderEnum.TOKEN_NAME);
    }
}
