package com.oing.config;

import com.oing.config.filter.WebRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webRequestInterceptor);
    }
}
