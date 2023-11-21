package com.oing.config;

import com.oing.config.filter.JwtAccessDeniedHandler;
import com.oing.config.filter.JwtAuthenticationEntryPoint;
import com.oing.config.filter.JwtAuthenticationHandler;
import com.oing.config.properties.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 4:38 PM
 */
@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig {
    /**
     * JWT 엑세스 토큰을 통한 인증 처리기
     */
    private final JwtAuthenticationHandler authenticationHandler;

    /**
     * 인증/인가 관련 실패 처리기
     */
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPoint entryPoint;

    private final WebProperties webProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requestsManagement ->{
                        for(String whitelistUrlPath : webProperties.urlWhitelists()) {
                            requestsManagement.requestMatchers(whitelistUrlPath).permitAll();
                        }
                        requestsManagement.anyRequest().authenticated();
                })
                .addFilterBefore(authenticationHandler, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandlerManagement ->
                        exceptionHandlerManagement
                                .authenticationEntryPoint(entryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }
}
