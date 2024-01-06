package com.oing.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.exception.ErrorCode;
import com.oing.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException {
        writeErrorResponse(response, authException);
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
