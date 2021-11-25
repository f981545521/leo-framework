package cn.acyou.leo.framework.interceptor;

import cn.acyou.leo.framework.constant.Constant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-25 9:46]
 */
public class AttachmentInterceptor extends BaseInterceptor {

    @Override
    protected String getToken(HttpServletRequest request) {
        return request.getParameter(Constant.TOKEN_NAME);
    }
}
