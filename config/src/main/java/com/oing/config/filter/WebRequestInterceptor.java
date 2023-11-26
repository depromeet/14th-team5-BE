package com.oing.config.filter;

import com.oing.config.properties.WebProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 5:47 PM
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebRequestInterceptor implements HandlerInterceptor {
    private static final String START_TIME_ATTR_NAME = "startTime";

    private final WebProperties webProperties;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {
        if (isPreflight(request) || isSwaggerRequest(request)) {
            return true;
        }
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR_NAME, startTime);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex
    ) throws Exception {
        if (webProperties.isNoLoggable(request.getServletPath())) return;

        long startTime = (long) request.getAttribute(START_TIME_ATTR_NAME);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        String forwardedIp = request.getHeader(webProperties.headerNames().proxyForwardHeader());
        String originIp = forwardedIp != null ? forwardedIp : request.getRemoteAddr();
        log.info("{} {} {} {}ms", request.getMethod(), request.getRequestURI(), originIp, executionTime);
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}
