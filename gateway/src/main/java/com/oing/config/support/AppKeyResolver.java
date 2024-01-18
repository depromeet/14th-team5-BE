package com.oing.config.support;

import com.google.common.base.Preconditions;
import com.oing.config.properties.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AppKeyResolver implements HandlerMethodArgumentResolver {
    private final WebProperties webProperties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestAppKey.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String appKey = webRequest.getHeader(webProperties.headerNames().appKeyHeader());
        Preconditions.checkNotNull(appKey, "App key is null");
        return UUID.fromString(appKey);
    }
}
