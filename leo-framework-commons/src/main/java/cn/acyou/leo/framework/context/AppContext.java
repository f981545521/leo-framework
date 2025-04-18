package cn.acyou.leo.framework.context;

import cn.acyou.leo.framework.annotation.authz.RequiresLogin;
import cn.acyou.leo.framework.annotation.authz.RequiresPermissions;
import cn.acyou.leo.framework.annotation.authz.RequiresRoles;
import cn.acyou.leo.framework.base.ClientLanguage;
import cn.acyou.leo.framework.base.ClientType;
import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.Result;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/8]
 **/
public class AppContext {
    //IP地址
    private static final ThreadLocal<String> IP_TL = new ThreadLocal<>();
    //请求TOKEN
    private static final ThreadLocal<String> TOKEN_TL = new ThreadLocal<>();
    //登录用户信息
    private static final ThreadLocal<LoginUser> LOGIN_USER_TL = new ThreadLocal<>();
    //客户端类型
    private static final ThreadLocal<ClientType> CLIENT_TYPE_TL = new ThreadLocal<>();
    //客户端语言
    private static final ThreadLocal<ClientLanguage> CLIENT_LANGUAGE_TL = new ThreadLocal<>();
    //请求路径
    private static final ThreadLocal<String> ACTION_URL_TL = new ThreadLocal<>();
    //请求接口注解：{@link ApiOperation}的Value
    private static final ThreadLocal<String[]> ACTION_API_OPERATION_TL = new ThreadLocal<>();
    //请求时间戳
    private static final ThreadLocal<Long> REQUEST_TIMESTAMP_TL = new ThreadLocal<>();
    //异常Result
    private static final ThreadLocal<Result<?>> EXCEPTION_RESULT_TL = new ThreadLocal<>();
    //请求体
    private static final ThreadLocal<String> REQUEST_BODY_TL = new ThreadLocal<>();

    private static final List<ThreadLocal<?>> THREAD_LOCAL_LIST = new ArrayList<>();

    static {
        THREAD_LOCAL_LIST.add(IP_TL);
        THREAD_LOCAL_LIST.add(TOKEN_TL);
        THREAD_LOCAL_LIST.add(LOGIN_USER_TL);
        THREAD_LOCAL_LIST.add(CLIENT_TYPE_TL);
        THREAD_LOCAL_LIST.add(CLIENT_LANGUAGE_TL);
        THREAD_LOCAL_LIST.add(ACTION_URL_TL);
        THREAD_LOCAL_LIST.add(ACTION_API_OPERATION_TL);
        THREAD_LOCAL_LIST.add(REQUEST_TIMESTAMP_TL);
        THREAD_LOCAL_LIST.add(EXCEPTION_RESULT_TL);
        THREAD_LOCAL_LIST.add(REQUEST_BODY_TL);
    }

    public static String getIp() {
        return IP_TL.get();
    }

    public static void setIp(String ip) {
        IP_TL.set(ip);
    }

    public static void clearIp() {
        IP_TL.remove();
    }

    public static String getToken() {
        return TOKEN_TL.get();
    }

    public static void setToken(String token) {
        TOKEN_TL.set(token);
    }

    public static void clearToken() {
        TOKEN_TL.remove();
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUserId();
    }

    public static Long checkGetUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new ServiceException(CommonErrorEnum.E_UNAUTHENTICATED);
        }
        return loginUser.getUserId();
    }

    public static LoginUser getLoginUser() {
        return LOGIN_USER_TL.get();
    }

    public static void setLoginUser(LoginUser user) {
        LOGIN_USER_TL.set(user);
    }

    public static void clearLoginUser() {
        LOGIN_USER_TL.remove();
    }

    public static String getActionUrl() {
        return ACTION_URL_TL.get();
    }

    public static void setActionUrl(String action) {
        ACTION_URL_TL.set(action);
    }

    public static void setActionApiOperation(String[] actionApiOperation){
        ACTION_API_OPERATION_TL.set(actionApiOperation);
    }

    public static String[] getActionApiMethodInfo(){
        return ACTION_API_OPERATION_TL.get();
    }

    public static String getActionApiMethodInfoRemark(){
        String[] values = ACTION_API_OPERATION_TL.get();
        if (values != null && values.length > 0){
            return values[0];
        }
        return "";

    }

    public static void clearActionApiMethodInfo(){
        ACTION_API_OPERATION_TL.remove();
    }

    public static String getActionApiOperationValue(){
        String[] values = ACTION_API_OPERATION_TL.get();
        if (values != null && values.length > 1){
            return values[1];
        }
        return "";
    }
    public static boolean getActionDebug(){
        String[] values = ACTION_API_OPERATION_TL.get();
        if (values != null && values.length > 2){
            return Boolean.parseBoolean(values[2]);
        }
        return Boolean.TRUE;
    }

    public static void clearAction() {
        ACTION_URL_TL.remove();
    }

    public static ClientType getClientType() {
        return CLIENT_TYPE_TL.get();
    }

    public static void setClientType(ClientType clientType) {
        CLIENT_TYPE_TL.set(clientType);
    }

    public static void clearClientType() {
        CLIENT_TYPE_TL.remove();
    }

    public static Long getRequestTimeStamp() {
        return REQUEST_TIMESTAMP_TL.get();
    }

    public static void setRequestTimeStamp(Long timeStamp) {
        REQUEST_TIMESTAMP_TL.set(timeStamp);
    }

    public static void clearRequestTimeStamp(){
        REQUEST_TIMESTAMP_TL.remove();
    }
    public static Result<?> getExceptionResult() {
        return EXCEPTION_RESULT_TL.get();
    }

    public static void setExceptionResult(Result<?> exceptionResult) {
        EXCEPTION_RESULT_TL.set(exceptionResult);
    }

    public static void clearExceptionResult(){
        EXCEPTION_RESULT_TL.remove();
    }

    public static String getRequestBody(){
        return REQUEST_BODY_TL.get();
    }

    public static void setRequestBody(String requestBodyStr) {
        REQUEST_BODY_TL.set(requestBodyStr);
    }

    public static void clearRequestParams() {
        REQUEST_BODY_TL.remove();
    }

    public static ClientLanguage getClientLanguage(){
        return CLIENT_LANGUAGE_TL.get();
    }

    public static void setClientLanguage(ClientLanguage clientLanguage){
        CLIENT_LANGUAGE_TL.set(clientLanguage);
    }

    public static void clearThreadLocal() {
        for (ThreadLocal<?> tl : THREAD_LOCAL_LIST) {
            if (tl != null) {
                tl.remove();
            }
        }
    }

    public static AppContextBean convertAppContextBean() {
        return AppContextBean.builder()
                .ip(getIp())
                .loginUser(getLoginUser())
                .clientType(getClientType())
                .clientLanguage(getClientLanguage())
                .actionUrl(getActionUrl())
                .actionApiOperation(ACTION_API_OPERATION_TL.get())
                .requestTimestamp(getRequestTimeStamp())
                .exceptionResult(getExceptionResult())
                .requestBodyStr(getRequestBody())
                .build();
    }

    @Data
    @Builder
    public static class AppContextBean {
        private String ip;
        private LoginUser loginUser;
        private ClientType clientType;
        private ClientLanguage clientLanguage;
        private String actionUrl;
        private String[] actionApiOperation;
        private Long requestTimestamp;
        private Result<?> exceptionResult;
        private String requestBodyStr;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MethodInfoBean {
        private String methodInfo = "";
        private String apiRemark = "";
        private String debug = "false";
        private boolean printRequestBody = true;
        private boolean printResponseBody = false;
        private ApiOperation apiOperation;
        private RequiresLogin requiresLogin;
        private RequiresRoles requiresRoles;
        private RequiresPermissions requiresPermissions;
    }


}
