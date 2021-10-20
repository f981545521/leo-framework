package cn.acyou.leo.framework.commons.dubbo;

import cn.acyou.leo.framework.context.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author fangyou
 * @version [1.0.0, 2021-10-20 14:13]
 */

@Slf4j
@Activate(group = {"APP_CONTEXT_GROUP"})
public class AppContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        AppContext.AppContextBean appContextBean = AppContext.convertAppContextBean();
        if (appContextBean == null) {
            appContextBean = (AppContext.AppContextBean) RpcContext.getContext().getObjectAttachment("DUBBO_APP_CONTEXT_KEY");
        }
        AppContext.setIp(appContextBean.getIp());
        AppContext.setLoginUser(appContextBean.getLoginUser());
        AppContext.setClientType(appContextBean.getClientType());
        AppContext.setClientLanguage(appContextBean.getClientLanguage());
        AppContext.setActionUrl(appContextBean.getActionUrl());
        AppContext.setActionApiOperation(appContextBean.getActionApiOperation());
        AppContext.setRequestTimeStamp(appContextBean.getRequestTimestamp());
        AppContext.setExceptionResult(appContextBean.getExceptionResult());
        AppContext.setRequestParams(appContextBean.getParams());
        RpcContext.getContext().getObjectAttachments().put("DUBBO_APP_CONTEXT_KEY", appContextBean);
        return invoker.invoke(invocation);
    }
}
