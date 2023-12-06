package com.oing.config.filter;

import com.oing.config.authentication.APIKeyAuthentication;
import com.oing.config.properties.WebProperties;
import com.oing.domain.Token;
import com.oing.domain.TokenType;
import com.oing.domain.exception.TokenNotValidException;
import com.oing.service.TokenGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 5:04 PM
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationHandler extends OncePerRequestFilter {
    private final WebProperties webProperties;
    private final TokenGenerator tokenGenerator;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = request.getHeader(webProperties.headerNames().accessToken());
        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Token token = tokenGenerator.extractTokenData(accessToken);
            Authentication authentication = new APIKeyAuthentication(token, token.userId(),
                    token.tokenType() == TokenType.TEMPORARY);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(TokenNotValidException ignored) {

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return webProperties.isWhitelisted(path);
    }
}
