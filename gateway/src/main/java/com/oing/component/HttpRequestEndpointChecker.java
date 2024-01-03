package com.oing.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.exception.ErrorCode;
import com.oing.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Objects;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 5:01 PM
 */
@Component
@RequiredArgsConstructor
public class HttpRequestEndpointChecker {
    private final ObjectMapper objectMapper;
    private final DispatcherServlet servlet;

    public boolean isEndpointExists(HttpServletRequest request) {
        for (HandlerMapping mapping : Objects.requireNonNull(servlet.getHandlerMappings())) {
            try {
                return mapping.getHandler(request) != null;
            } catch (Exception ignored) {
                return false;
            }
        }
        return false;
    }

    public void writeNotFoundException(
            HttpServletResponse response
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.API_PATH_NOT_FOUND)));
    }
}
