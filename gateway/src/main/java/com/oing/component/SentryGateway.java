package com.oing.component;

import com.oing.config.properties.WebProperties;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.sentry.protocol.Request;
import io.sentry.protocol.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SentryGateway {

    private final WebProperties webProperties;

    public void captureException(Throwable exception, SentryLevel sentryLevel, HttpServletRequest request, HttpStatus statusCode) {
        // Add extra data to future events in this scope.
        Sentry.withScope(scope -> {
            removeDefaultTags(scope);

            scope.setLevel(sentryLevel);
            setRequestHttp(scope, request, statusCode);
            setRequestUser(scope, request);

            Sentry.captureException(exception);
        });
    }

    // 잘못된 디폴트 값으로 혼란을 야기시키는 태그들을 제거합니다.
    private void removeDefaultTags(Scope scope) {
        scope.removeTag("client_os");
        scope.removeTag("environment");
        scope.removeTag("runtime");
        scope.removeTag("runtime.name");
        scope.removeTag("server_name");
    }

    private void setRequestHttp(Scope scope, HttpServletRequest servletRequest, HttpStatus statusCode) {
        Request request = new Request();

        request.setMethod(servletRequest.getMethod());

        if (!servletRequest.getRequestURL().isEmpty()) {
            request.setUrl(servletRequest.getRequestURL().toString());
        }

        if (!servletRequest.getQueryString().isEmpty()) {
            request.setQueryString(servletRequest.getQueryString());
        }

        if (servletRequest.getContentLength() > 0) {
            request.setData(getBody(servletRequest));
        }

        if (!servletRequest.getParameterMap().isEmpty()) {
            Map<String, String> parameterMap = new HashMap<>();
            for (String key : servletRequest.getParameterMap().keySet()) {
                parameterMap.put(key, servletRequest.getParameter(key));
            }
            request.setOthers(parameterMap);
        }

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, servletRequest.getHeader(headerName));
        }
        request.setHeaders(headers);

        scope.setRequest(request);
        scope.setTag("http.status_code", String.valueOf(statusCode.value()));
    }

    private String getBody(HttpServletRequest request) {
        try (
                InputStream inputStream = request.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }

            return stringBuilder.toString();

        } catch (IOException e) {
            return "Error reading the request body";
        }
    }

    private void setRequestUser(Scope scope, HttpServletRequest request) {
        String appVersion = request.getHeader(webProperties.headerNames().appVersionHeader());
        String platform = request.getHeader(webProperties.headerNames().platformHeader());
        scope.setTag("client_os.name", platform);
        scope.setTag("app_version", appVersion);

        String userId = request.getHeader(webProperties.headerNames().userIdHeader());
        String forwardedIp = request.getHeader(webProperties.headerNames().proxyForwardHeader());
        String originIp = forwardedIp != null ? forwardedIp : request.getRemoteAddr();

        User user = new User();
        user.setId(userId);
        user.setIpAddress(originIp);
        scope.setUser(user);
    }
}