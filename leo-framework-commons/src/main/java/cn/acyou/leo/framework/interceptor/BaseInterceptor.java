package cn.acyou.leo.framework.interceptor;

import cn.acyou.leo.framework.base.ClientLanguage;
import cn.acyou.leo.framework.base.InterfaceCallStatistics;
import cn.acyou.leo.framework.base.LoginUser;
import cn.acyou.leo.framework.commons.AsyncManager;
import cn.acyou.leo.framework.commons.RedisKeyConstant;
import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.prop.LeoProperty;
import cn.acyou.leo.framework.service.UserTokenService;
import cn.acyou.leo.framework.util.CacheUtil;
import cn.acyou.leo.framework.util.IPUtil;
import cn.acyou.leo.framework.util.SourceUtil;
import cn.acyou.leo.framework.util.redis.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基本的Token拦截器
 *
 * @author fangyou
 * @version [1.0.0, 2021-11-25 9:46]
 */
@Slf4j
public abstract class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private LeoProperty leoProperty;
    @Autowired(required = false)
    private UserTokenService userTokenService;

    AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 获得Token的方式
     *
     * @param request 请求
     * @return {@link String}
     */
    protected abstract String getToken(HttpServletRequest request);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        AppContext.setRequestTimeStamp(System.currentTimeMillis());
        String requestURI = request.getRequestURI();
        final String requestMethod = request.getMethod();
        String remoteIp = IPUtil.getClientIp(request);

        final Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() > 0) {
            List<String> params = new ArrayList<>();
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                params.add(stringEntry.getKey() + "=" + stringEntry.getValue()[0]);
            }
            final String joinParam = StringUtils.collectionToDelimitedString(params, "&");
            requestURI = requestURI + "?" + joinParam;
        }
        String logMessage = String.format("访问开始 ——> %s [%s %s] ", remoteIp, requestMethod, requestURI);
        String requestBody = "";
        if (leoProperty.isPrintRequestBody()) {
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                InputStream is = request.getInputStream ();
                StringBuilder responseStrBuilder = new StringBuilder ();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String inputStr;
                while ((inputStr = streamReader.readLine ()) != null){
                    responseStrBuilder.append (inputStr);
                }
                requestBody = responseStrBuilder.toString();
            }
            if (requestBody.length() > 0) {
                AppContext.setRequestBody(requestBody);
                logMessage = logMessage + String.format(" 请求体: %s", requestBody);
            }
        }
        log.info(logMessage);
        //启用Token校验
        if (leoProperty.isTokenVerify() && !isMatcherPath(request.getRequestURI())) {
            String token = getToken(request);
            if (!StringUtils.hasText(token)) {
                falseResult(response, CommonErrorEnum.E_UNAUTHENTICATED);
                return false;
            }
            String userId = redisUtils.get(RedisKeyConstant.USER_LOGIN_TOKEN + token);
            if (!StringUtils.hasText(userId)) {
                String loginAtOtherWhere = redisUtils.get(RedisKeyConstant.USER_LOGIN_AT_OTHER_WHERE + token);
                if (StringUtils.hasText(loginAtOtherWhere)) {
                    falseResult(response, CommonErrorEnum.E_LOGIN_AT_OTHER_WHERE);
                }else {
                    falseResult(response, CommonErrorEnum.E_LOGIN_TIMEOUT);
                }
                return false;
            }
            LoginUser loginUser = userTokenService.getLoginUserByUserId(Long.valueOf(userId));
            AppContext.setLoginUser(loginUser);
        }
        AppContext.setIp(remoteIp);

        AppContext.setActionUrl(requestURI);
        AppContext.setClientType(SourceUtil.getClientTypeByUserAgent(request));
        String language = request.getHeader("Language");
        AppContext.setClientLanguage(ClientLanguage.getLanguage(language));
        AppContext.MethodInfoBean methodInfoBean = new AppContext.MethodInfoBean();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            final String methodInfo = handlerMethod.toString();
            methodInfoBean = CacheUtil.getAndCache("BaseInterceptor.methodDebug." + methodInfo, 0L, (k) -> {
                Method method = handlerMethod.getMethod();
                String apiRemark = "";
                ApiOperation annotation = method.getAnnotation(ApiOperation.class);
                if (annotation != null) {
                    apiRemark = annotation.value();
                    Extension[] extensions = annotation.extensions();
                    if (extensions != null) {
                        for (Extension extension : extensions) {
                            if (extension.properties() != null) {
                                for (ExtensionProperty property : extension.properties()) {
                                    if (property.name().equalsIgnoreCase("debug")) {
                                        if (property.value().equalsIgnoreCase("false")) {
                                            return new AppContext.MethodInfoBean(methodInfo, apiRemark, "false");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return new AppContext.MethodInfoBean(methodInfo, apiRemark, "true");
            });
        }
        AppContext.setActionApiOperation(new String[]{methodInfoBean.getMethodInfo(), methodInfoBean.getApiRemark(), methodInfoBean.getDebug()});
        return true;
    }

    private boolean isMatcherPath(String path){
        List<String> ignoreUriList = leoProperty.getIgnoreUriList();
        for (String url : ignoreUriList) {
            if (pathMatcher.match(url, path)) {
                return true;
            }
        }
        return false;
    }

    private void falseResult(HttpServletResponse response, CommonErrorEnum commonErrorEnum) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Result<String> resultBody = Result.error(commonErrorEnum);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(resultBody));
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        processInterfaceStatistics(request);
        final Long requestTimeStamp = AppContext.getRequestTimeStamp();
        String logMessage = String.format("访问结束 <——  [status:%s 请求耗时:%s ms]",
                response.getStatus(),
                requestTimeStamp != null ? (System.currentTimeMillis() - AppContext.getRequestTimeStamp()) : "-");
        if (leoProperty.isPrintResponseBody()) {
            String contentType = response.getContentType();
            if (contentType != null && contentType.contains("application/json")) {
                ContentCachingResponseWrapper resp =  WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
                if (resp != null) {
                    String responseBody = StreamUtils.copyToString(resp.getContentInputStream(), StandardCharsets.UTF_8);
                    logMessage = logMessage + String.format(" 响应体: %s", responseBody.length() > 1000 ? responseBody.substring(0, 1000) : responseBody);
                }
            }
        }
        log.info(logMessage);
        //clear context
        AppContext.clearThreadLocal();
        PageHelper.clearPage();
    }

    /**
     * 接口统计分析
     * @param request request
     */
    public void processInterfaceStatistics(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        if (!leoProperty.isInterfaceCallStatistics()) {
            return;
        }
        if (leoProperty.getIgnoreUriList().contains(requestURI)){
            return;
        }
        if (!AppContext.getActionDebug()) {
            return;
        }
        Result<?> exceptionResult = AppContext.getExceptionResult();
        InterfaceCallStatistics interfaceCallStatistics =
                InterfaceCallStatistics.builder()
                        .url(requestURI)
                        .type(Constant.CONS_1)
                        .methodType(request.getMethod())
                        .methodInfo(AppContext.getActionApiMethodInfoRemark())
                        .methodDesc(AppContext.getActionApiOperationValue())
                        .params(AppContext.getRequestBody())
                        .startTime(new Date(AppContext.getRequestTimeStamp()))
                        .execTime(System.currentTimeMillis() - AppContext.getRequestTimeStamp())
                        .errorMessage(exceptionResult != null ? exceptionResult.getMessage() : null)
                        .errorStackTrace(exceptionResult != null && exceptionResult.getData() != null ? JSON.toJSONString(exceptionResult.getData()) : null)
                        .clientType(AppContext.getClientType().getMessage())
                        .ip(AppContext.getIp())
                        .userId(AppContext.getLoginUser() != null ? AppContext.getLoginUser().getUserId() : null)
                        .userName(AppContext.getLoginUser() != null ? AppContext.getLoginUser().getUserName() : null)
                        .build();
        AsyncManager.execute(() -> {
            log.info(">>> {}", interfaceCallStatistics);
        });
    }

}
