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
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
@Slf4j
public class SpringMvcInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private LeoProperty leoProperty;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteIp = IPUtil.getClientIp(request);
        String localIp = IPUtil.getLocalIP();
        log.info("SpringMvcInterceptor ——>  remoteIP:{}, localIP: {}, 访问路径:{}", remoteIp, localIp, request.getRequestURI());
        String token = request.getHeader(Constant.TOKEN_NAME);
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
        //TODO: MustBe LoginUser loginUser = userTokenService.getLoginUser(token);
        String loginStr = redisUtils.get(RedisKeyConstant.USER_LOGIN_INFO + userId);
        AppContext.setLoginUser(JSON.parseObject(loginStr, LoginUser.class));
        AppContext.setIp(remoteIp);
        AppContext.setRequestTimeStamp(System.currentTimeMillis());
        AppContext.setActionUrl(request.getRequestURI());
        AppContext.setClientType(SourceUtil.getClientTypeByUserAgent(request));
        String language = request.getHeader("Language");
        AppContext.setClientLanguage(ClientLanguage.getLanguage(language));
        String methodInfo = "";
        String apiRemark = "";
        String debug = "true";
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            methodInfo = handlerMethod.toString();
            Method method = handlerMethod.getMethod();
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
                                        debug = "false";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        AppContext.setActionApiOperation(new String[]{methodInfo, apiRemark, debug});
        return true;
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
        log.info("SpringMvcInterceptor ——>  访问结束。");
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
                        .params(AppContext.getParamsMap() != null && AppContext.getParamsMap().size() > 0 ? JSON.toJSONString(AppContext.getParamsMap()) : null)
                        .startTime(new Date(AppContext.getRequestTimeStamp()))
                        .execTime(System.currentTimeMillis() - AppContext.getRequestTimeStamp())
                        .errorMessage(exceptionResult != null ? exceptionResult.getMessage() : null)
                        .errorStackTrace(exceptionResult != null && exceptionResult.getData() != null ? JSON.toJSONString(exceptionResult.getData()) : null)
                        .clientType(AppContext.getClientType().getMessage())
                        .ip(AppContext.getIp())
                        .userId(AppContext.getLoginUser() != null ? AppContext.getLoginUser().getUserId() : null)
                        .userName(AppContext.getLoginUser() != null ? AppContext.getLoginUser().getUserName() : null)
                        .build();
        AsyncManager.me().execute(() -> {
            log.info(">>> {}", interfaceCallStatistics);
        });
    }


}
