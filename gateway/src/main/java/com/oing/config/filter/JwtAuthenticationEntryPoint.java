package com.oing.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.component.SentryGateway;
import com.oing.dto.response.ErrorResponse;
import com.oing.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.sentry.SentryLevel.WARNING;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 4:47 PM
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final SentryGateway sentryGateway;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException {
        writeErrorResponse(response, authException);
        sentryGateway.captureException(authException, WARNING, request, UNAUTHORIZED);
    }

    private void writeErrorResponse(
            HttpServletResponse response, AuthenticationException authException
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED)));
    }
}
