package com.oing.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.component.AppVersionCache;
import com.oing.config.properties.WebProperties;
import com.oing.dto.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/04
 * Time: 5:04 PM
 */
@RequiredArgsConstructor
@Component
public class AppVersionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final WebProperties webProperties;
    private final AppVersionCache appVersionCache;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String appKey = request.getHeader(webProperties.headerNames().appKeyHeader());
        if (appKey == null) {
            writeUpdateResponse(response, false);
            return;
        }
        UUID appKeyUUID = UUID.fromString(appKey);
        if (!appVersionCache.isServiceable(appKeyUUID)) {
            writeUpdateResponse(response, true);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return webProperties.isWhitelisted(path);
    }

    private void writeUpdateResponse(
            HttpServletResponse response,
            boolean isTokenExist
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(426);
        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        ErrorResponse.of("UR0001",
                                isTokenExist ? "App Version Required Downgrade!!" : "App Key Not Found"
                        )
                ));
    }
}
