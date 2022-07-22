package cn.acyou.leo.framework.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/22 14:49]
 **/
@Component
public class MdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put("leoTraceNo", UUID.randomUUID().toString().replaceAll("-", ""));
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
