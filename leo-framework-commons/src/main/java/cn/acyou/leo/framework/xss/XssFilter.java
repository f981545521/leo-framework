package cn.acyou.leo.framework.xss;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 防止XSS攻击的过滤器
 *
 * @author youfang
 */
public class XssFilter implements Filter {
    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    /**
     * xss过滤开关
     */
    public boolean enabled = true;

    /**
     * 路径匹配器
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        String tempEnabled = filterConfig.getInitParameter("enabled");
        if (StringUtils.hasText(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            excludes.addAll(Arrays.asList(url));
        }
        if (StringUtils.hasText(tempEnabled)) {
            enabled = Boolean.parseBoolean(tempEnabled);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapperV3 xssRequest = new XssHttpServletRequestWrapperV3((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(xssRequest, responseWrapper);
        if (xssRequest.isAsyncStarted()) {
            //DeferredResult Support
            xssRequest.getAsyncContext().addListener(new AsyncListener() {
                public void onComplete(AsyncEvent asyncEvent) throws IOException {
                    responseWrapper.copyBodyToResponse();
                }

                public void onTimeout(AsyncEvent asyncEvent) {
                }

                public void onError(AsyncEvent asyncEvent) {
                }

                public void onStartAsync(AsyncEvent asyncEvent) {
                }
            });
        } else {
            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        //过滤webSocket
        String requestURI = request.getRequestURI();
        if (requestURI.contains("socket")) {
            return true;
        }
        if (!enabled) {
            return true;
        }
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        for (String pattern : excludes) {
            if (pathMatcher.match(pattern, url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}