package cn.acyou.leo.framework.aspect;

import cn.acyou.leo.framework.model.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xiaoymin.knife4j.spring.filter.BasicFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Knife4j 禁用拦截器
 * @author youfang
 * @version [1.0.0, 2022-3-10]
 **/
public class ApiDocSecurityFilter extends BasicFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        if (!match(uri)) {
            chain.doFilter(request, response);
        } else {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(JSON.toJSONString(Result.error("404 error"), SerializerFeature.WriteMapNullValue));
            pw.flush();
        }
    }

    @Override
    public void destroy() {

    }

    public ApiDocSecurityFilter() {

    }

}
