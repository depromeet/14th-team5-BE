package com.oing.util.security;

import com.oing.exception.FamilyIdNotFoundException;
import com.oing.service.MemberBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class FamilyIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberBridge memberBridge;

    @Autowired
    public FamilyIdArgumentResolver(MemberBridge memberBridge) {
        this.memberBridge = memberBridge;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FamilyId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        String familyId = memberBridge.getFamilyIdByMemberId(memberId);
        if (familyId == null) {
            throw new FamilyIdNotFoundException();
        }
        return familyId;
    }
}
