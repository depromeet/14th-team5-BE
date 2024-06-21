package com.oing.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.oing.config.filter.WebRequestInterceptor;
import com.oing.config.support.AppKeyResolver;
import com.oing.util.security.LoginFamilyIdArgumentResolver;
import com.oing.util.security.LoginMemberIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 5:46 PM
 */
@RequiredArgsConstructor
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
    final WebRequestInterceptor webRequestInterceptor;
    final AppKeyResolver appKeyResolver;
    final LoginFamilyIdArgumentResolver familyIdArgumentResolver;
    final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;

    @Value("${app.oauth.google-client-id}")
    private String googleClientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webRequestInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(appKeyResolver);
        resolvers.add(familyIdArgumentResolver);
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://bibbi.app", "https://no5ing.kr", "https://dev.no5ing.kr")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
